package com.synth.hotelbookingmanagement.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {
    @org.springframework.data.jpa.repository.Query(
            nativeQuery = true,
            value = "SELECT * FROM hotels WHERE (setweight(to_tsvector('english', coalesce(name, '')), 'A') || setweight(to_tsvector('english', coalesce(email, '')), 'B') || setweight(to_tsvector('english', coalesce(address, '')), 'C') || setweight(to_tsvector('english', coalesce(city, '')), 'C') || setweight(to_tsvector('english', coalesce(country, '')), 'C')) @@ plainto_tsquery('english', :q) ORDER BY ts_rank((setweight(to_tsvector('english', coalesce(name, '')), 'A') || setweight(to_tsvector('english', coalesce(email, '')), 'B') || setweight(to_tsvector('english', coalesce(address, '')), 'C') || setweight(to_tsvector('english', coalesce(city, '')), 'C') || setweight(to_tsvector('english', coalesce(country, '')), 'C')), plainto_tsquery('english', :q)) DESC")
    java.util.List<Hotel> searchHotels(@org.springframework.data.repository.query.Param("q") String q);
}
