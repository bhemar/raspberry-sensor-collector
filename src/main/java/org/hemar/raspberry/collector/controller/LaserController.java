package org.hemar.raspberry.collector.controller;
import lombok.AllArgsConstructor;
import org.hemar.raspberry.collector.sensor.LaserSensor;
import org.hemar.raspberry.collector.utils.ThreadUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@ConditionalOnProperty(value = "sensor.laser.enabled", havingValue = "true")
public class LaserController {

    private final LaserSensor laserSensor;

    @GetMapping("/laser/toggle")
    public void toggle() {
        laserSensor.toggle();
    }

    @GetMapping("/laser/blink")
    public void blink() {
        int count = 0;
        while(count < 50) {
            laserSensor.toggle();
            count++;
            ThreadUtils.sleepUnchecked(500);
        }
    }
}
