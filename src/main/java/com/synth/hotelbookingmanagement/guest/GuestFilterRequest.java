package com.synth.hotelbookingmanagement.guest;

import org.springframework.lang.Nullable;

public record GuestFilterRequest(
        @Nullable String nationality
) {
    public GuestFilterRequest() {
        this(null);
    }
}
