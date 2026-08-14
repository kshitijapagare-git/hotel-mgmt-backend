package com.synth.hotelbookingmanagement.reservation;

public final class ReservationMapper {

    private ReservationMapper() {}

    public static Reservation toEntity(ReservationCreateRequest request) {
        Reservation entity = new Reservation();
        entity.setCheckInDate(request.checkInDate());
        entity.setCheckOutDate(request.checkOutDate());
        entity.setTotalAmount(request.totalAmount());
        entity.setStatus(request.status());
        entity.setSpecialRequests(request.specialRequests());
        return entity;
    }

    public static void updateEntity(Reservation entity, ReservationUpdateRequest request) {
        entity.setCheckInDate(request.checkInDate());
        entity.setCheckOutDate(request.checkOutDate());
        entity.setTotalAmount(request.totalAmount());
        entity.setStatus(request.status());
        entity.setSpecialRequests(request.specialRequests());
    }
}
