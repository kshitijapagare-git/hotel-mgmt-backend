package com.synth.hotelbookingmanagement.reservation;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class ReservationSpecification {

    public static Specification<Reservation> build(ReservationFilterRequest filter) {
        return Specification.where(roomId(filter))
                .and(guestId(filter))
                .and(status(filter));
    }

    private static Specification<Reservation> roomId(ReservationFilterRequest filter) {
        return (root, query, cb) -> filter.roomId() == null ? null :
                cb.equal(root.get("roomId"), filter.roomId());
    }

    private static Specification<Reservation> guestId(ReservationFilterRequest filter) {
        return (root, query, cb) -> filter.guestId() == null ? null :
                cb.equal(root.get("guestId"), filter.guestId());
    }

    private static Specification<Reservation> status(ReservationFilterRequest filter) {
        return (root, query, cb) -> filter.status() == null ? null :
                cb.equal(root.get("status"), filter.status());
    }
}
