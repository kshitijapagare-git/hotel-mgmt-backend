package com.synth.hotelbookingmanagement.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code RENOVATION} status processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class RenovationHotelStrategy implements HotelProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(RenovationHotelStrategy.class);

    @Override
    public boolean supports(String status) {
        return "RENOVATION".equals(status);
    }

    @Override
    public void execute(Hotel entity) {
        log.debug("Executing RENOVATION strategy for {} id={}", "Hotel", entity.getId());
        // TODO: add RENOVATION-specific processing here
    }
}
