package com.synth.hotelbookingmanagement.room;

import com.synth.hotelbookingmanagement.exception.RoomNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.synth.hotelbookingmanagement.hotel.Hotel;
import com.synth.hotelbookingmanagement.hotel.HotelRepository;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repository;
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private RoomProcessingStrategy strategy;
    private RoomStrategyFactory strategyFactory;
    private RoomService service;

    private static final UUID EXISTING_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID MISSING_ID  = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private Room fixture;

    @BeforeEach
    void seedFixture() {
        fixture = new Room();
        
        ReflectionTestUtils.setField(fixture, "id", EXISTING_ID);
        fixture.setRoomNumber("sample-room-number");
        fixture.setType("sample-type");
        fixture.setFloor(42);
        fixture.setPricePerNight(new java.math.BigDecimal("42.00"));
        fixture.setCapacity(42);
        fixture.setStatus("sample-status");
        strategyFactory = new RoomStrategyFactory(List.of(strategy));
        service = new RoomService(repository, hotelRepository, strategyFactory);
    }

    // ── findAll ────────────────────────────────────────────────────────────────

    @Test
    void should_return_all_responses_when_repository_has_entities() {
        Pageable pageable = PageRequest.of(0, 20);
        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(fixture)));
        Page<RoomResponse> result = service.findAll(new RoomFilterRequest(), pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    // ── findById ───────────────────────────────────────────────────────────────

    @Test
    void should_return_response_when_entity_exists_by_id() {
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));

        RoomResponse result = service.findById(EXISTING_ID);

        assertThat(result.id()).isEqualTo(EXISTING_ID);
        verify(repository).findById(EXISTING_ID);
    }

    @Test
    void should_throw_entity_not_found_when_id_is_unknown() {
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(MISSING_ID))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));
    }

    // ── create ─────────────────────────────────────────────────────────────────

    @Test
    void should_persist_entity_and_return_response_when_create_is_called() {
        RoomCreateRequest request = new RoomCreateRequest("test-value", "test-value", 1, new java.math.BigDecimal("1.00"), 1, "test-value", UUID.fromString("11111111-1111-1111-1111-111111111111"));
        when(hotelRepository.findById(any())).thenReturn(Optional.of(new Hotel()));
        when(strategy.supports(any())).thenReturn(true);
        when(repository.save(any(Room.class))).thenReturn(fixture);

        RoomResponse result = service.create(request);

        assertThat(result.id()).isEqualTo(fixture.getId());
        assertThat(result.roomNumber()).isEqualTo(fixture.getRoomNumber());
        
        ArgumentCaptor<Room> createCaptor = ArgumentCaptor.forClass(Room.class);
        verify(repository).save(createCaptor.capture());
        assertThat(createCaptor.getValue().getRoomNumber()).isEqualTo(request.roomNumber());
        
    }

    // ── update ─────────────────────────────────────────────────────────────────

    @Test
    void should_update_entity_and_return_response_when_entity_exists() {
        RoomUpdateRequest request = new RoomUpdateRequest("updated-value", "updated-value", 2, new java.math.BigDecimal("2.00"), 2, "updated-value", UUID.fromString("22222222-2222-2222-2222-222222222222"));
        when(hotelRepository.findById(any())).thenReturn(Optional.of(new Hotel()));
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));
        when(repository.save(any(Room.class))).thenReturn(fixture);

        RoomResponse result = service.update(EXISTING_ID, request);

        assertThat(result.id()).isEqualTo(EXISTING_ID);
        assertThat(result.roomNumber()).isEqualTo(fixture.getRoomNumber());
        verify(repository).findById(EXISTING_ID);
        ArgumentCaptor<Room> updateCaptor = ArgumentCaptor.forClass(Room.class);
        verify(repository).save(updateCaptor.capture());
        assertThat(updateCaptor.getValue().getRoomNumber()).isEqualTo(request.roomNumber());
        
    }

    @Test
    void should_throw_entity_not_found_when_updating_with_unknown_id() {
        RoomUpdateRequest request = new RoomUpdateRequest("updated-value", "updated-value", 2, new java.math.BigDecimal("2.00"), 2, "updated-value", UUID.fromString("22222222-2222-2222-2222-222222222222"));
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(MISSING_ID, request))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));

        verify(repository).findById(MISSING_ID);
        verify(repository, never()).save(any(Room.class));
    }

    // ── delete ─────────────────────────────────────────────────────────────────

    @Test
    void should_remove_entity_when_id_exists() {
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));

        service.delete(EXISTING_ID);

        verify(repository).findById(EXISTING_ID);
        verify(repository).delete(fixture);
    }

    @Test
    void should_throw_entity_not_found_when_deleting_with_unknown_id() {
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(MISSING_ID))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));

        verify(repository, never()).delete(any(Room.class));
    }
}
