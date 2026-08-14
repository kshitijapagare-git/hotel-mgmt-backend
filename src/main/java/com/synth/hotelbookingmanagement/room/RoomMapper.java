package com.synth.hotelbookingmanagement.room;

public final class RoomMapper {

    private RoomMapper() {}

    public static Room toEntity(RoomCreateRequest request) {
        Room entity = new Room();
        entity.setRoomNumber(request.roomNumber());
        entity.setType(request.type());
        entity.setFloor(request.floor());
        entity.setPricePerNight(request.pricePerNight());
        entity.setCapacity(request.capacity());
        entity.setStatus(request.status());
        return entity;
    }

    public static void updateEntity(Room entity, RoomUpdateRequest request) {
        entity.setRoomNumber(request.roomNumber());
        entity.setType(request.type());
        entity.setFloor(request.floor());
        entity.setPricePerNight(request.pricePerNight());
        entity.setCapacity(request.capacity());
        entity.setStatus(request.status());
    }
}
