package com.synth.hotelbookingmanagement.room;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class RoomSpecification {

    public static Specification<Room> build(RoomFilterRequest filter) {
        return Specification.where(hotelId(filter))
                .and(type(filter))
                .and(status(filter));
    }

    private static Specification<Room> hotelId(RoomFilterRequest filter) {
        return (root, query, cb) -> filter.hotelId() == null ? null :
                cb.equal(root.get("hotelId"), filter.hotelId());
    }

    private static Specification<Room> type(RoomFilterRequest filter) {
        return (root, query, cb) -> filter.type() == null ? null :
                cb.equal(root.get("type"), filter.type());
    }

    private static Specification<Room> status(RoomFilterRequest filter) {
        return (root, query, cb) -> filter.status() == null ? null :
                cb.equal(root.get("status"), filter.status());
    }
}
