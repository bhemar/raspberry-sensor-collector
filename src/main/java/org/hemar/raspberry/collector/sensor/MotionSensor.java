package org.hemar.raspberry.collector.sensor;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hemar.raspberry.collector.service.GpioPinProvider;
import org.hemar.raspberry.collector.config.MetricFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "sensor.motion.enabled", havingValue = "true")
public class MotionSensor {

    private final GpioPinProvider gpioPinProvider;
    private final MetricFactory metricFactory;

    private Counter motionDetected;

    @Value("${sensor.motion.gpio-pin}")
    private int gpioPin;

    @PostConstruct
    public void init() {
        motionDetected = metricFactory.createAndRegisterCounter("motion_sensor");

        gpioPinProvider.getDigitalInput(RaspiPin.getPinByAddress(gpioPin))
                .addTrigger(
                        new GpioCallbackTrigger(PinState.HIGH, this::motionStarted),
                        new GpioCallbackTrigger(PinState.LOW, this::motionEnded)
                );

        log.info("Initialized motion sensor");
    }

    private Void motionStarted() {
        log.debug("Motion started");
        motionDetected.increment();
        return null;
    }

    private Void motionEnded() {
        log.debug("Motion ended");
        return null;
    }
}
