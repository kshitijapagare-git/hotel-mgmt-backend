package com.synth.hotelbookingmanagement.reservation;
import jakarta.validation.constraints.*;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by PUT /reservations/{id}.
 *
 * Distinct from ReservationCreateRequest so the MapStruct mapper can
 * apply NullValuePropertyMappingStrategy.IGNORE — null fields in the
 * incoming payload leave the entity's existing value untouched.
 */
public record ReservationUpdateRequest(
        @NotNull LocalDate checkInDate,
        @NotNull LocalDate checkOutDate,
        @Min(0) BigDecimal totalAmount,
        @Pattern(regexp = "^(CONFIRMED|CHECKED_IN|CHECKED_OUT|CANCELLED|NO_SHOW)$") @NotBlank String status,
        @Size(max = 500) String specialRequests,
        UUID roomId,
        UUID guestId
) {}
