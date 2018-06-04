package org.hemar.raspberry.collector.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Use to create and register metric components.
 */
@Component
@RequiredArgsConstructor
public class MetricFactory {

    private static final String METRIC_PREFIX = "sensor_collector_service_";

    private final MeterRegistry meterRegistry;

    public Counter createAndRegisterCounter(@NonNull String name) {
        return Counter.builder(METRIC_PREFIX + name).register(meterRegistry);
    }

    public AtomicInteger createAndRegisterGauge(@NonNull String name) {
        AtomicInteger gauge = new AtomicInteger();

        Gauge.builder(METRIC_PREFIX + name, gauge, AtomicInteger::get).register(meterRegistry);

        return gauge;
    }
}
