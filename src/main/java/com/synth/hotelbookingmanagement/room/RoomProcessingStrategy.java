package com.synth.hotelbookingmanagement.room;

/**
 * Strategy for Room processing, dispatched by the {@code type} field.
 *
 * Implement this interface and annotate with {@code @Component} to support a new
 * variant — no changes required in service or factory code (Open/Closed Principle).
 */
public interface RoomProcessingStrategy {

    /** Returns true when this strategy handles the given type value. */
    boolean supports(String type);

    /** Apply strategy-specific logic to the entity before it is persisted. */
    void execute(Room entity);
}
