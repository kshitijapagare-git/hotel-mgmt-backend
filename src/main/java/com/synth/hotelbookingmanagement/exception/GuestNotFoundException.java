package com.synth.hotelbookingmanagement.exception;
import java.util.UUID;

public class GuestNotFoundException extends ResourceNotFoundException {

    public GuestNotFoundException(UUID id) {
        super("Guest not found: " + id);
    }
}
