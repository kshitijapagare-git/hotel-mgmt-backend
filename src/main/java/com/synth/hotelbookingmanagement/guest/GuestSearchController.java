package com.synth.hotelbookingmanagement.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/guests")
@Tag(name = "Guest Search")
public class GuestSearchController {

    private final GuestRepository repository;

    @Transactional(readOnly = true)
    @GetMapping("/search-guests")
    @Operation(summary = "Search guests across firstName, lastName, email")
    public List<GuestResponse> searchGuests(
            @RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) return List.of();
        return repository.searchGuests(search).stream()
                .map(GuestResponse::from)
                .toList();
    }
}
