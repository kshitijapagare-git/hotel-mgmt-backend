package com.synth.hotelbookingmanagement.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Resolves the correct {@link RoomProcessingStrategy} at runtime.
 *
 * Uses the Null Object pattern: when no registered strategy matches, a no-op
 * implementation is returned so the application never throws on an unknown
 * variant — behaviour can be upgraded by registering a new {@code @Component}.
 */
@Component
public class RoomStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(RoomStrategyFactory.class);

    private final List<RoomProcessingStrategy> strategies;

    public RoomStrategyFactory(List<RoomProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    public RoomProcessingStrategy resolve(String type) {
        return strategies.stream()
                .filter(s -> s.supports(type))
                .findFirst()
                .orElseGet(() -> new NoOpStrategy(type));
    }

    /** Null Object — logs a warning and does nothing when no strategy is registered. */
    private static final class NoOpStrategy implements RoomProcessingStrategy {
        private final String variant;
        NoOpStrategy(String variant) { this.variant = variant; }

        @Override
        public boolean supports(String value) { return true; }

        @Override
        public void execute(Room entity) {
            log.warn("No RoomProcessingStrategy registered for type='{}'; applying no-op (Null Object).", variant);
        }
    }
}
