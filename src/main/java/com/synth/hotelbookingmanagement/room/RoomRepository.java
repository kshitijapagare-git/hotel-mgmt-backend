package com.synth.hotelbookingmanagement.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID>, JpaSpecificationExecutor<Room> {
    @org.springframework.data.jpa.repository.Query(
            nativeQuery = true,
            value = "SELECT * FROM rooms WHERE (setweight(to_tsvector('english', coalesce(room_number, '')), 'B') || setweight(to_tsvector('english', coalesce(type, '')), 'C') || setweight(to_tsvector('english', coalesce(status, '')), 'C')) @@ plainto_tsquery('english', :q) ORDER BY ts_rank((setweight(to_tsvector('english', coalesce(room_number, '')), 'B') || setweight(to_tsvector('english', coalesce(type, '')), 'C') || setweight(to_tsvector('english', coalesce(status, '')), 'C')), plainto_tsquery('english', :q)) DESC")
    java.util.List<Room> searchRooms(@org.springframework.data.repository.query.Param("q") String q);
}
