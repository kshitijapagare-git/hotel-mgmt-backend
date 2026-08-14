package com.synth.hotelbookingmanagement.reservation;

import org.springframework.lang.Nullable;
import java.util.UUID;

public record ReservationFilterRequest(
        @Nullable UUID roomId,
        @Nullable UUID guestId,
        @Nullable String status
) {
    public ReservationFilterRequest() {
        this(null, null, null);
    }
}
