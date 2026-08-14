package com.synth.hotelbookingmanagement.reservation;
import jakarta.validation.constraints.*;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Payload accepted by POST /reservations.
 *
 * Emitted as a separate type from ReservationUpdateRequest so MapStruct
 * can map them with different policies — create-time we want to populate
 * server-managed fields; update-time we want to ignore null values.
 */
public record ReservationCreateRequest(
        @NotNull LocalDate checkInDate,
        @NotNull LocalDate checkOutDate,
        @Min(0) BigDecimal totalAmount,
        @Pattern(regexp = "^(CONFIRMED|CHECKED_IN|CHECKED_OUT|CANCELLED|NO_SHOW)$") @NotBlank String status,
        @Size(max = 500) String specialRequests,
        @NotNull UUID roomId,
        @NotNull UUID guestId
) {
}
