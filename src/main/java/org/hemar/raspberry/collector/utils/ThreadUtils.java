package org.hemar.raspberry.collector.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ThreadUtils {

    public static void sleepUnchecked(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Sleep interrupted: {}", e.getMessage(), e);
        }
    }
}
