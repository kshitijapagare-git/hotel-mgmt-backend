package com.synth.hotelbookingmanagement.exception;
import java.util.UUID;

public class ReservationNotFoundException extends ResourceNotFoundException {

    public ReservationNotFoundException(UUID id) {
        super("Reservation not found: " + id);
    }
}
