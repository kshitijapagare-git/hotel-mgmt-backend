package com.synth.hotelbookingmanagement.room;
import jakarta.validation.constraints.*;
import java.util.UUID;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by PUT /rooms/{id}.
 *
 * Distinct from RoomCreateRequest so the MapStruct mapper can
 * apply NullValuePropertyMappingStrategy.IGNORE — null fields in the
 * incoming payload leave the entity's existing value untouched.
 */
public record RoomUpdateRequest(
        @NotBlank @Size(max = 20) String roomNumber,
        @Pattern(regexp = "^(SINGLE|DOUBLE|SUITE|DELUXE|PENTHOUSE)$") @NotBlank String type,
        Integer floor,
        @NotNull @Min(0) BigDecimal pricePerNight,
        @Min(1) Integer capacity,
        @Pattern(regexp = "^(AVAILABLE|OCCUPIED|MAINTENANCE|RESERVED)$") @NotBlank String status,
        UUID hotelId
) {}
