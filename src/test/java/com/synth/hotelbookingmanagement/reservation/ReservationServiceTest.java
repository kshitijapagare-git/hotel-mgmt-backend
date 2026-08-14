package com.synth.hotelbookingmanagement.reservation;

import com.synth.hotelbookingmanagement.exception.ReservationNotFoundException;
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
import com.synth.hotelbookingmanagement.room.Room;
import com.synth.hotelbookingmanagement.room.RoomRepository;
import com.synth.hotelbookingmanagement.guest.Guest;
import com.synth.hotelbookingmanagement.guest.GuestRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private ReservationProcessingStrategy strategy;
    private ReservationStrategyFactory strategyFactory;
    private ReservationService service;

    private static final UUID EXISTING_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID MISSING_ID  = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private Reservation fixture;

    @BeforeEach
    void seedFixture() {
        fixture = new Reservation();
        
        ReflectionTestUtils.setField(fixture, "id", EXISTING_ID);
        fixture.setTotalAmount(new java.math.BigDecimal("42.00"));
        fixture.setStatus("sample-status");
        fixture.setSpecialRequests("sample-special-requests");
        strategyFactory = new ReservationStrategyFactory(List.of(strategy));
        service = new ReservationService(repository, roomRepository, guestRepository, strategyFactory);
    }

    // ── findAll ────────────────────────────────────────────────────────────────

    @Test
    void should_return_all_responses_when_repository_has_entities() {
        Pageable pageable = PageRequest.of(0, 20);
        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(fixture)));
        Page<ReservationResponse> result = service.findAll(new ReservationFilterRequest(), pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    // ── findById ───────────────────────────────────────────────────────────────

    @Test
    void should_return_response_when_entity_exists_by_id() {
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));

        ReservationResponse result = service.findById(EXISTING_ID);

        assertThat(result.id()).isEqualTo(EXISTING_ID);
        verify(repository).findById(EXISTING_ID);
    }

    @Test
    void should_throw_entity_not_found_when_id_is_unknown() {
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(MISSING_ID))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));
    }

    // ── create ─────────────────────────────────────────────────────────────────

    @Test
    void should_persist_entity_and_return_response_when_create_is_called() {
        ReservationCreateRequest request = new ReservationCreateRequest(java.time.LocalDate.of(2024, 1, 1), java.time.LocalDate.of(2024, 1, 1), new java.math.BigDecimal("1.00"), "test-value", "test-value", UUID.fromString("11111111-1111-1111-1111-111111111111"), UUID.fromString("11111111-1111-1111-1111-111111111111"));
        when(roomRepository.findById(any())).thenReturn(Optional.of(new Room()));
        when(guestRepository.findById(any())).thenReturn(Optional.of(new Guest()));
        when(strategy.supports(any())).thenReturn(true);
        when(repository.save(any(Reservation.class))).thenReturn(fixture);

        ReservationResponse result = service.create(request);

        assertThat(result.id()).isEqualTo(fixture.getId());
        assertThat(result.checkInDate()).isEqualTo(fixture.getCheckInDate());
        
        ArgumentCaptor<Reservation> createCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(repository).save(createCaptor.capture());
        assertThat(createCaptor.getValue().getCheckInDate()).isEqualTo(request.checkInDate());
        
    }

    // ── update ─────────────────────────────────────────────────────────────────

    @Test
    void should_update_entity_and_return_response_when_entity_exists() {
        ReservationUpdateRequest request = new ReservationUpdateRequest(java.time.LocalDate.of(2025, 6, 1), java.time.LocalDate.of(2025, 6, 1), new java.math.BigDecimal("2.00"), "updated-value", "updated-value", UUID.fromString("22222222-2222-2222-2222-222222222222"), UUID.fromString("22222222-2222-2222-2222-222222222222"));
        when(roomRepository.findById(any())).thenReturn(Optional.of(new Room()));
        when(guestRepository.findById(any())).thenReturn(Optional.of(new Guest()));
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));
        when(repository.save(any(Reservation.class))).thenReturn(fixture);

        ReservationResponse result = service.update(EXISTING_ID, request);

        assertThat(result.id()).isEqualTo(EXISTING_ID);
        assertThat(result.checkInDate()).isEqualTo(fixture.getCheckInDate());
        verify(repository).findById(EXISTING_ID);
        ArgumentCaptor<Reservation> updateCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(repository).save(updateCaptor.capture());
        assertThat(updateCaptor.getValue().getCheckInDate()).isEqualTo(request.checkInDate());
        
    }

    @Test
    void should_throw_entity_not_found_when_updating_with_unknown_id() {
        ReservationUpdateRequest request = new ReservationUpdateRequest(java.time.LocalDate.of(2025, 6, 1), java.time.LocalDate.of(2025, 6, 1), new java.math.BigDecimal("2.00"), "updated-value", "updated-value", UUID.fromString("22222222-2222-2222-2222-222222222222"), UUID.fromString("22222222-2222-2222-2222-222222222222"));
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(MISSING_ID, request))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));

        verify(repository).findById(MISSING_ID);
        verify(repository, never()).save(any(Reservation.class));
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
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));

        verify(repository, never()).delete(any(Reservation.class));
    }
}
