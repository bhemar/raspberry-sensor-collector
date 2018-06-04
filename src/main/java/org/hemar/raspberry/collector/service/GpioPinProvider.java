package org.hemar.raspberry.collector.service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GpioPinProvider {

    private final GpioController gpio;

    private final Map<Integer, GpioPinDigitalInput> inputCache = new HashMap<>();
    private final Map<Integer, GpioPinDigitalOutput> outputCache = new HashMap<>();

    /**
     * Provides digital input for pin.
     */
    public synchronized GpioPinDigitalInput getDigitalInput(@NonNull Pin pin) {
        return inputCache.computeIfAbsent(pin.getAddress(), gpioPinId -> provideDigitalInput(pin));
    }

    /**
     * Provides digital output for pin.
     */
    public synchronized GpioPinDigitalOutput getDigitalOutput(@NonNull Pin pin) {
        return outputCache.computeIfAbsent(pin.getAddress(), gpioPinId -> provideDigitalOutput(pin));
    }

    private GpioPinDigitalInput provideDigitalInput(Pin pin) {
        log.info("Provisioning input on pin {}...", pin);
        GpioPinDigitalInput gpioPinDigitalInput = gpio.provisionDigitalInputPin(pin);
        log.info("Provisioned input on pin {}", pin);
        return gpioPinDigitalInput;
    }

    private GpioPinDigitalOutput provideDigitalOutput(Pin pin) {
        log.info("Provisioning output on pin {}...", pin);
        GpioPinDigitalOutput gpioPinDigitalOutput = gpio.provisionDigitalOutputPin(pin);
        log.info("Provisioned output on pin {}.", pin);
        return gpioPinDigitalOutput;
    }
}
