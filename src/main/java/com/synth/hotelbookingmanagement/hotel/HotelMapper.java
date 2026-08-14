package com.synth.hotelbookingmanagement.hotel;

public final class HotelMapper {

    private HotelMapper() {}

    public static Hotel toEntity(HotelCreateRequest request) {
        Hotel entity = new Hotel();
        entity.setName(request.name());
        entity.setAddress(request.address());
        entity.setCity(request.city());
        entity.setCountry(request.country());
        entity.setStarRating(request.starRating());
        entity.setPhone(request.phone());
        entity.setEmail(request.email());
        entity.setStatus(request.status());
        return entity;
    }

    public static void updateEntity(Hotel entity, HotelUpdateRequest request) {
        entity.setName(request.name());
        entity.setAddress(request.address());
        entity.setCity(request.city());
        entity.setCountry(request.country());
        entity.setStarRating(request.starRating());
        entity.setPhone(request.phone());
        entity.setEmail(request.email());
        entity.setStatus(request.status());
    }
}
