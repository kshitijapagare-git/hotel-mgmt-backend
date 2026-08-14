package com.synth.hotelbookingmanagement.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles {@code DOUBLE} type processing.
 *
 * Extend this class or add collaborators here to implement variant-specific
 * business rules without touching any other class (Single Responsibility + OCP).
 */
@Component
public class DoubleRoomStrategy implements RoomProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(DoubleRoomStrategy.class);

    @Override
    public boolean supports(String type) {
        return "DOUBLE".equals(type);
    }

    @Override
    public void execute(Room entity) {
        log.debug("Executing DOUBLE strategy for {} id={}", "Room", entity.getId());
        // TODO: add DOUBLE-specific processing here
    }
}
