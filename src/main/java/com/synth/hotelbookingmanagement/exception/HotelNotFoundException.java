package com.synth.hotelbookingmanagement.exception;
import java.util.UUID;

public class HotelNotFoundException extends ResourceNotFoundException {

    public HotelNotFoundException(UUID id) {
        super("Hotel not found: " + id);
    }
}
