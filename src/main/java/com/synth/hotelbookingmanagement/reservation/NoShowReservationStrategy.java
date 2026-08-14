package com.synth.hotelbookingmanagement.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code NO_SHOW} status processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class NoShowReservationStrategy implements ReservationProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(NoShowReservationStrategy.class);

    @Override
    public boolean supports(String status) {
        return "NO_SHOW".equals(status);
    }

    @Override
    public void execute(Reservation entity) {
        log.debug("Executing NO_SHOW strategy for {} id={}", "Reservation", entity.getId());
        // TODO: add NO_SHOW-specific processing here
    }
}
