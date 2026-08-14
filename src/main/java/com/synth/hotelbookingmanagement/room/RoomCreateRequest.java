package com.synth.hotelbookingmanagement.room;
import jakarta.validation.constraints.*;
import java.util.UUID;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by POST /rooms.
 *
 * Emitted as a separate type from RoomUpdateRequest so MapStruct
 * can map them with different policies — create-time we want to populate
 * server-managed fields; update-time we want to ignore null values.
 */
public record RoomCreateRequest(
        @NotBlank @Size(max = 20) String roomNumber,
        @Pattern(regexp = "^(SINGLE|DOUBLE|SUITE|DELUXE|PENTHOUSE)$") @NotBlank String type,
        Integer floor,
        @NotNull @Min(0) BigDecimal pricePerNight,
        @Min(1) Integer capacity,
        @Pattern(regexp = "^(AVAILABLE|OCCUPIED|MAINTENANCE|RESERVED)$") @NotBlank String status,
        @NotNull UUID hotelId
) {
}
