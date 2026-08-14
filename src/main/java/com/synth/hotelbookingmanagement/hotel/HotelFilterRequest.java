package com.synth.hotelbookingmanagement.hotel;

import org.springframework.lang.Nullable;

public record HotelFilterRequest(
        @Nullable String city,
        @Nullable String status
) {
    public HotelFilterRequest() {
        this(null, null);
    }
}
