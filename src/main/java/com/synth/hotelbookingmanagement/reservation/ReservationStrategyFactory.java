package com.synth.hotelbookingmanagement.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Resolves the correct {@link ReservationProcessingStrategy} at runtime.
 *
 * Uses the Null Object pattern: when no registered strategy matches, a no-op
 * implementation is returned so the application never throws on an unknown
 * variant — behaviour can be upgraded by registering a new {@code @Component}.
 */
@Component
public class ReservationStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(ReservationStrategyFactory.class);

    private final List<ReservationProcessingStrategy> strategies;

    public ReservationStrategyFactory(List<ReservationProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    public ReservationProcessingStrategy resolve(String status) {
        return strategies.stream()
                .filter(s -> s.supports(status))
                .findFirst()
                .orElseGet(() -> new NoOpStrategy(status));
    }

    /** Null Object — logs a warning and does nothing when no strategy is registered. */
    private static final class NoOpStrategy implements ReservationProcessingStrategy {
        private final String variant;
        NoOpStrategy(String variant) { this.variant = variant; }

        @Override
        public boolean supports(String value) { return true; }

        @Override
        public void execute(Reservation entity) {
            log.warn("No ReservationProcessingStrategy registered for status='{}'; applying no-op (Null Object).", variant);
        }
    }
}
