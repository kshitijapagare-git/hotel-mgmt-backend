package com.synth.hotelbookingmanagement.guest;
import jakarta.validation.constraints.*;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by POST /guests.
 *
 * Emitted as a separate type from GuestUpdateRequest so MapStruct
 * can map them with different policies — create-time we want to populate
 * server-managed fields; update-time we want to ignore null values.
 */
public record GuestCreateRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") @Size(max = 255) String email,
        @NotBlank @Size(max = 20) String phone,
        @Size(max = 100) String nationality,
        @Size(max = 50) String passportNumber
) {
}
