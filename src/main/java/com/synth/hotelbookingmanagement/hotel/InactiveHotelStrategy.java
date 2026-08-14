package com.synth.hotelbookingmanagement.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code INACTIVE} status processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class InactiveHotelStrategy implements HotelProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(InactiveHotelStrategy.class);

    @Override
    public boolean supports(String status) {
        return "INACTIVE".equals(status);
    }

    @Override
    public void execute(Hotel entity) {
        log.debug("Executing INACTIVE strategy for {} id={}", "Hotel", entity.getId());
        // TODO: add INACTIVE-specific processing here
    }
}
