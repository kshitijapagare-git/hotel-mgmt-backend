package com.synth.hotelbookingmanagement.room;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Room Search")
public class RoomSearchController {

    private final RoomRepository repository;

    @Transactional(readOnly = true)
    @GetMapping("/search-rooms")
    @Operation(summary = "Search rooms across roomNumber, type, status")
    public List<RoomResponse> searchRooms(
            @RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) return List.of();
        return repository.searchRooms(search).stream()
                .map(RoomResponse::from)
                .toList();
    }
}
