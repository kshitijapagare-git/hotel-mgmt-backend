package com.synth.hotelbookingmanagement.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code CONFIRMED} status processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class ConfirmedReservationStrategy implements ReservationProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(ConfirmedReservationStrategy.class);

    @Override
    public boolean supports(String status) {
        return "CONFIRMED".equals(status);
    }

    @Override
    public void execute(Reservation entity) {
        log.debug("Executing CONFIRMED strategy for {} id={}", "Reservation", entity.getId());
        // TODO: add CONFIRMED-specific processing here
    }
}
