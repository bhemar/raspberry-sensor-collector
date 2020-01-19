package org.hemar.raspberry.collector.sensor;

import com.pi4j.io.gpio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hemar.raspberry.collector.config.ConditionalOnLaserSensorEnabled;
import org.hemar.raspberry.collector.service.GpioPinProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Manage laser sensor/module.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnLaserSensorEnabled
public class LaserSensor {

    private final GpioPinProvider gpioPinProvider;

    @Value("${sensor.laser.gpio-pin}")
    private int gpioPin;

    public synchronized void toggle() {
        getPinOutput().toggle();
        log.debug("Laser toggle triggered.");
    }

    private GpioPinDigitalOutput getPinOutput() {
        return gpioPinProvider.getDigitalOutput(RaspiPin.getPinByAddress(gpioPin));
    }
}
