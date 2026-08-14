package com.synth.hotelbookingmanagement.event;

import java.time.Instant;

/**
 * Published after every successful lifecycle operation (create, update, delete).
 * Use {@code @TransactionalEventListener(phase = AFTER_COMMIT)} to observe safely.
 */
public record EntityLifecycleEvent(
        String entityType,
        Object entityId,
        String operation,
        Instant occurredAt
) {}
