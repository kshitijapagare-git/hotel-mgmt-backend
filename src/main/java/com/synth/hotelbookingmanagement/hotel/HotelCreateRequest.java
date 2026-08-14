package com.synth.hotelbookingmanagement.hotel;
import jakarta.validation.constraints.*;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by POST /hotels.
 *
 * Emitted as a separate type from HotelUpdateRequest so MapStruct
 * can map them with different policies — create-time we want to populate
 * server-managed fields; update-time we want to ignore null values.
 */
public record HotelCreateRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 500) String address,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 100) String country,
        @Min(1) @Max(5) Integer starRating,
        @NotBlank @Size(max = 20) String phone,
        @NotBlank @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") @Size(max = 255) String email,
        @Pattern(regexp = "^(ACTIVE|INACTIVE|RENOVATION)$") @NotBlank String status
) {
}
