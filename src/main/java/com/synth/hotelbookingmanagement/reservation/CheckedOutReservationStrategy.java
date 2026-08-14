package com.synth.hotelbookingmanagement.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code CHECKED_OUT} status processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class CheckedOutReservationStrategy implements ReservationProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(CheckedOutReservationStrategy.class);

    @Override
    public boolean supports(String status) {
        return "CHECKED_OUT".equals(status);
    }

    @Override
    public void execute(Reservation entity) {
        log.debug("Executing CHECKED_OUT strategy for {} id={}", "Reservation", entity.getId());
        // TODO: add CHECKED_OUT-specific processing here
    }
}
