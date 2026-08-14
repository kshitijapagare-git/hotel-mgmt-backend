package com.synth.hotelbookingmanagement.reservation;
import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import com.synth.hotelbookingmanagement.room.Room;
import com.synth.hotelbookingmanagement.guest.Guest;

public record ReservationResponse(
        UUID id,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalAmount,
        String status,
        String specialRequests,
        RoomSummary roomId,
        GuestSummary guestId
) {
    public record RoomSummary(UUID id, String roomNumber, String type, Integer floor, BigDecimal pricePerNight, Integer capacity, String status) {}
    public record GuestSummary(UUID id, String firstName, String lastName, String email, String phone, String nationality, String passportNumber) {}

    public static ReservationResponse from(Reservation entity) {
        return new ReservationResponse(
entity.getId(),
entity.getCheckInDate(),
entity.getCheckOutDate(),
entity.getTotalAmount(),
entity.getStatus(),
entity.getSpecialRequests(),
entity.getRoomId() != null
                        ? new RoomSummary(entity.getRoomId().getId(), entity.getRoomId().getRoomNumber(), entity.getRoomId().getType(), entity.getRoomId().getFloor(), entity.getRoomId().getPricePerNight(), entity.getRoomId().getCapacity(), entity.getRoomId().getStatus())
                        : null,
entity.getGuestId() != null
                        ? new GuestSummary(entity.getGuestId().getId(), entity.getGuestId().getFirstName(), entity.getGuestId().getLastName(), entity.getGuestId().getEmail(), entity.getGuestId().getPhone(), entity.getGuestId().getNationality(), entity.getGuestId().getPassportNumber())
                        : null
        );
    }
}
