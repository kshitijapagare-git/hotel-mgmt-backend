package com.synth.hotelbookingmanagement.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<Guest, UUID>, JpaSpecificationExecutor<Guest> {
    @org.springframework.data.jpa.repository.Query(
            nativeQuery = true,
            value = "SELECT * FROM guests WHERE (setweight(to_tsvector('english', coalesce(first_name, '')), 'A') || setweight(to_tsvector('english', coalesce(last_name, '')), 'A') || setweight(to_tsvector('english', coalesce(email, '')), 'B') || setweight(to_tsvector('english', coalesce(passport_number, '')), 'B') || setweight(to_tsvector('english', coalesce(phone, '')), 'C')) @@ plainto_tsquery('english', :q) ORDER BY ts_rank((setweight(to_tsvector('english', coalesce(first_name, '')), 'A') || setweight(to_tsvector('english', coalesce(last_name, '')), 'A') || setweight(to_tsvector('english', coalesce(email, '')), 'B') || setweight(to_tsvector('english', coalesce(passport_number, '')), 'B') || setweight(to_tsvector('english', coalesce(phone, '')), 'C')), plainto_tsquery('english', :q)) DESC")
    java.util.List<Guest> searchGuests(@org.springframework.data.repository.query.Param("q") String q);
}
