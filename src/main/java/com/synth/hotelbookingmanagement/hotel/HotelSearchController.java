package com.synth.hotelbookingmanagement.hotel;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/hotels")
@Tag(name = "Hotel Search")
public class HotelSearchController {

    private final HotelRepository repository;

    @Transactional(readOnly = true)
    @GetMapping("/search-hotels")
    @Operation(summary = "Search hotels across name, email, address")
    public List<HotelResponse> searchHotels(
            @RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) return List.of();
        return repository.searchHotels(search).stream()
                .map(HotelResponse::from)
                .toList();
    }
}
