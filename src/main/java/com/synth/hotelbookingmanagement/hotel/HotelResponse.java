package com.synth.hotelbookingmanagement.hotel;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

public record HotelResponse(
        UUID id,
        String name,
        String address,
        String city,
        String country,
        Integer starRating,
        String phone,
        String email,
        String status
) {

    public static HotelResponse from(Hotel entity) {
        return new HotelResponse(
entity.getId(),
entity.getName(),
entity.getAddress(),
entity.getCity(),
entity.getCountry(),
entity.getStarRating(),
entity.getPhone(),
entity.getEmail(),
entity.getStatus()
        );
    }
}
