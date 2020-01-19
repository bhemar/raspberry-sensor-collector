package org.hemar.raspberry.collector.controller;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hemar.raspberry.collector.config.ConditionalOnLaserSensorEnabled;
import org.hemar.raspberry.collector.sensor.LaserSensor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@ConditionalOnLaserSensorEnabled
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

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.warn("Sleep interrupted: {}", e.getMessage(), e);
                return;
            }
        }
    }
}
