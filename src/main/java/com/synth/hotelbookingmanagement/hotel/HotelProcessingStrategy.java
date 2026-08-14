package com.synth.hotelbookingmanagement.hotel;

/**
 * Strategy for Hotel processing, dispatched by the {@code status} field.
 *
 * Implement this interface and annotate with {@code @Component} to support a new
 * variant — no changes required in service or factory code (Open/Closed Principle).
 */
public interface HotelProcessingStrategy {

    /** Returns true when this strategy handles the given status value. */
    boolean supports(String status);

    /** Apply strategy-specific logic to the entity before it is persisted. */
    void execute(Hotel entity);
}
