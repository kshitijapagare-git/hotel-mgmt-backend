package com.synth.hotelbookingmanagement.room;
import java.util.UUID;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import com.synth.hotelbookingmanagement.hotel.Hotel;

public record RoomResponse(
        UUID id,
        String roomNumber,
        String type,
        Integer floor,
        BigDecimal pricePerNight,
        Integer capacity,
        String status,
        HotelSummary hotelId
) {
    public record HotelSummary(UUID id, String name, String address, String city, String country, Integer starRating, String phone, String email, String status) {}

    public static RoomResponse from(Room entity) {
        return new RoomResponse(
entity.getId(),
entity.getRoomNumber(),
entity.getType(),
entity.getFloor(),
entity.getPricePerNight(),
entity.getCapacity(),
entity.getStatus(),
entity.getHotelId() != null
                        ? new HotelSummary(entity.getHotelId().getId(), entity.getHotelId().getName(), entity.getHotelId().getAddress(), entity.getHotelId().getCity(), entity.getHotelId().getCountry(), entity.getHotelId().getStarRating(), entity.getHotelId().getPhone(), entity.getHotelId().getEmail(), entity.getHotelId().getStatus())
                        : null
        );
    }
}
