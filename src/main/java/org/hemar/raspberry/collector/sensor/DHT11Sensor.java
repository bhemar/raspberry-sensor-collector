package org.hemar.raspberry.collector.sensor;

import com.pi4j.wiringpi.Gpio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(value = "sensor.temperature-humidity.enabled", havingValue = "true")
public class DHT11Sensor {

    private static final int MAX_ATTEMPTS = 30;
    private static final int SLEEP_BETWEEN_RETRIES_DURATION_MILLIS = 1000;

    private static final int MAX_TIMINGS = 85;

    @Value("${sensor.temperature-humidity.gpio-pin}")
    private int gpioPin;

    Optional<SensorData> readSensorData() {
        int attempt = 0;

        while (attempt++ < MAX_ATTEMPTS) {
            SensorData data = readData();

            if (data != null) {
                return Optional.of(data);
            }

            try {
                Thread.sleep(SLEEP_BETWEEN_RETRIES_DURATION_MILLIS);
            } catch (InterruptedException e) {
                log.warn("Sleep interrupted: {}", e.getMessage(), e);
                return Optional.empty();
            }
        }

        log.warn("Could not read sensor data.");

        return Optional.empty();
    }

    private SensorData readData() {
        int[] dht11_dat = {0, 0, 0, 0, 0};

        int lastState = Gpio.HIGH;

        Gpio.pinMode(gpioPin, Gpio.OUTPUT);
        Gpio.digitalWrite(gpioPin, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(gpioPin, Gpio.HIGH);
        Gpio.pinMode(gpioPin, Gpio.INPUT);

        int j = 0;

        for (int i = 0; i < MAX_TIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(gpioPin) == lastState) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            lastState = Gpio.digitalRead(gpioPin);

            if (counter == 255) {
                break;
            }

            /* ignore first 3 transitions */
            if (i >= 4 && i % 2 == 0) {
                /* shove each bit into the storage bytes */
                dht11_dat[j / 8] <<= 1;
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last byte
        if (j >= 40 && checkParity(dht11_dat)) {
            float humidity = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (humidity > 100) {
                humidity = dht11_dat[0]; // for DHT11
            }
            float celsius = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if (celsius > 125) {
                celsius = dht11_dat[2]; // for DHT11
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                celsius = -celsius;
            }

            return new SensorData(humidity, celsius);
        } else {
            log.debug("Invalid data, skipping...");
        }
        return null;
    }

    private boolean checkParity(int[] dht11_dat) {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }

    @lombok.Value
    static class SensorData {

        private final double humidityPercentage;
        private final double temperatureCelsius;
    }
}
