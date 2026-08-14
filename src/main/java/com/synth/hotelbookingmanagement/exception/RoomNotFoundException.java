package com.synth.hotelbookingmanagement.exception;
import java.util.UUID;

public class RoomNotFoundException extends ResourceNotFoundException {

    public RoomNotFoundException(UUID id) {
        super("Room not found: " + id);
    }
}
