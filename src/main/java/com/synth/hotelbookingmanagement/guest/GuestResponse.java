package com.synth.hotelbookingmanagement.guest;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

public record GuestResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String nationality,
        String passportNumber
) {

    public static GuestResponse from(Guest entity) {
        return new GuestResponse(
entity.getId(),
entity.getFirstName(),
entity.getLastName(),
entity.getEmail(),
entity.getPhone(),
entity.getNationality(),
entity.getPassportNumber()
        );
    }
}
