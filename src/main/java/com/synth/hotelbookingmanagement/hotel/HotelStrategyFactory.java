package com.synth.hotelbookingmanagement.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Resolves the correct {@link HotelProcessingStrategy} at runtime.
 *
 * Uses the Null Object pattern: when no registered strategy matches, a no-op
 * implementation is returned so the application never throws on an unknown
 * variant — behaviour can be upgraded by registering a new {@code @Component}.
 */
@Component
public class HotelStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(HotelStrategyFactory.class);

    private final List<HotelProcessingStrategy> strategies;

    public HotelStrategyFactory(List<HotelProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    public HotelProcessingStrategy resolve(String status) {
        return strategies.stream()
                .filter(s -> s.supports(status))
                .findFirst()
                .orElseGet(() -> new NoOpStrategy(status));
    }

    /** Null Object — logs a warning and does nothing when no strategy is registered. */
    private static final class NoOpStrategy implements HotelProcessingStrategy {
        private final String variant;
        NoOpStrategy(String variant) { this.variant = variant; }

        @Override
        public boolean supports(String value) { return true; }

        @Override
        public void execute(Hotel entity) {
            log.warn("No HotelProcessingStrategy registered for status='{}'; applying no-op (Null Object).", variant);
        }
    }
}
