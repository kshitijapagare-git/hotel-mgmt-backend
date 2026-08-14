package com.synth.hotelbookingmanagement.hotel;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecification {

    public static Specification<Hotel> build(HotelFilterRequest filter) {
        return Specification.where(city(filter))
                .and(status(filter));
    }

    private static Specification<Hotel> city(HotelFilterRequest filter) {
        return (root, query, cb) -> filter.city() == null ? null :
                cb.like(cb.lower(root.get("city")), "%"+filter.city().toLowerCase()+"%");
    }

    private static Specification<Hotel> status(HotelFilterRequest filter) {
        return (root, query, cb) -> filter.status() == null ? null :
                cb.equal(root.get("status"), filter.status());
    }
}
