package org.hemar.raspberry.collector.sensor;

import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hemar.raspberry.collector.config.MetricFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reads data from temperature humidity sensor.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "sensor.temperature-humidity.enabled", havingValue = "true")
public class TemperatureHumidityMetrics {

    private static final int SENSOR_REFRESH_INTERVAL_SECONDS = 30;

    private final DHT11Sensor dht11Sensor;
    private final MetricFactory metricFactory;

    private AtomicInteger humidityPercentage;
    private AtomicInteger temperatureCelsius;

    @PostConstruct
    public void init() {
        humidityPercentage = metricFactory.createAndRegisterGauge("humidity_percentage");
        temperatureCelsius= metricFactory.createAndRegisterGauge("temperature_celsius");
    }

    @Scheduled(fixedDelay = SENSOR_REFRESH_INTERVAL_SECONDS * 1000)
    public void refreshData() {

        Stopwatch stopwatch = Stopwatch.createStarted();

        dht11Sensor.readSensorData().ifPresent(data -> {
            humidityPercentage.set((int) data.getHumidityPercentage());
            temperatureCelsius.set((int) data.getTemperatureCelsius());
            log.info("Refreshed sensor data in {} {}", stopwatch.stop(), data);
        });
    }
}
