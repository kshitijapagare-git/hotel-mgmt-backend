package com.synth.hotelbookingmanagement.guest;

public final class GuestMapper {

    private GuestMapper() {}

    public static Guest toEntity(GuestCreateRequest request) {
        Guest entity = new Guest();
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setNationality(request.nationality());
        entity.setPassportNumber(request.passportNumber());
        return entity;
    }

    public static void updateEntity(Guest entity, GuestUpdateRequest request) {
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setNationality(request.nationality());
        entity.setPassportNumber(request.passportNumber());
    }
}
