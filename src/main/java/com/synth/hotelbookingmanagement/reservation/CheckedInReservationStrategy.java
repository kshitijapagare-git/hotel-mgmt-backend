package com.synth.hotelbookingmanagement.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code CHECKED_IN} status processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class CheckedInReservationStrategy implements ReservationProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(CheckedInReservationStrategy.class);

    @Override
    public boolean supports(String status) {
        return "CHECKED_IN".equals(status);
    }

    @Override
    public void execute(Reservation entity) {
        log.debug("Executing CHECKED_IN strategy for {} id={}", "Reservation", entity.getId());
        // TODO: add CHECKED_IN-specific processing here
    }
}
