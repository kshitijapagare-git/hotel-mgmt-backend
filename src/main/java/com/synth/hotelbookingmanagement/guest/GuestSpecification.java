package com.synth.hotelbookingmanagement.guest;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class GuestSpecification {

    public static Specification<Guest> build(GuestFilterRequest filter) {
        return Specification.where(nationality(filter));
    }

    private static Specification<Guest> nationality(GuestFilterRequest filter) {
        return (root, query, cb) -> filter.nationality() == null ? null :
                cb.like(cb.lower(root.get("nationality")), "%"+filter.nationality().toLowerCase()+"%");
    }
}
