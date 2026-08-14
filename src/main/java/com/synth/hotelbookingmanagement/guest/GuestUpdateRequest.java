package com.synth.hotelbookingmanagement.guest;
import jakarta.validation.constraints.*;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by PUT /guests/{id}.
 *
 * Distinct from GuestCreateRequest so the MapStruct mapper can
 * apply NullValuePropertyMappingStrategy.IGNORE — null fields in the
 * incoming payload leave the entity's existing value untouched.
 */
public record GuestUpdateRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") @Size(max = 255) String email,
        @NotBlank @Size(max = 20) String phone,
        @Size(max = 100) String nationality,
        @Size(max = 50) String passportNumber
) {}
