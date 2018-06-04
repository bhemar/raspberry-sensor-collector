package org.hemar.raspberry.collector.config;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.wiringpi.Gpio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeansConfig {

    @Bean(destroyMethod = "shutdown")
    public GpioController gpioController() {
        if (Gpio.wiringPiSetup() == -1) {
            throw new IllegalStateException("wiring PI lib is missing");
        }
        return GpioFactory.getInstance();
    }

}
