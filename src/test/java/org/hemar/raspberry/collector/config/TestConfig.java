package org.hemar.raspberry.collector.config;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import org.hemar.raspberry.collector.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import(Application.class)
public class TestConfig {

    @Bean
    public GpioController gpioController() {
        GpioController mock = mock(GpioController.class);
        when(mock.provisionDigitalInputPin(any(Pin.class))).thenReturn(mock(GpioPinDigitalInput.class));
        when(mock.provisionDigitalOutputPin(any(Pin.class))).thenReturn(mock(GpioPinDigitalOutput.class));
        return mock;
    }
}
