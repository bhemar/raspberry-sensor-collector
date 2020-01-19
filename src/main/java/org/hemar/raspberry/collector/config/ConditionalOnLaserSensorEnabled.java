package org.hemar.raspberry.collector.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(value = "sensor.laser.enabled", havingValue = "true")
public @interface ConditionalOnLaserSensorEnabled {

}
