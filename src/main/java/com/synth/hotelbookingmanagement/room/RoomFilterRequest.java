package com.synth.hotelbookingmanagement.room;

import org.springframework.lang.Nullable;
import java.util.UUID;

public record RoomFilterRequest(
        @Nullable UUID hotelId,
        @Nullable String type,
        @Nullable String status
) {
    public RoomFilterRequest() {
        this(null, null, null);
    }
}
