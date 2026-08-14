package com.synth.hotelbookingmanagement.hotel;

import com.synth.hotelbookingmanagement.exception.HotelNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository repository;
    @Mock
    private HotelProcessingStrategy strategy;
    private HotelStrategyFactory strategyFactory;
    private HotelService service;

    private static final UUID EXISTING_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID MISSING_ID  = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private Hotel fixture;

    @BeforeEach
    void seedFixture() {
        fixture = new Hotel();
        
        ReflectionTestUtils.setField(fixture, "id", EXISTING_ID);
        fixture.setName("sample-name");
        fixture.setAddress("sample-address");
        fixture.setCity("sample-city");
        fixture.setCountry("sample-country");
        fixture.setStarRating(42);
        fixture.setPhone("sample-phone");
        fixture.setEmail("sample-email");
        fixture.setStatus("sample-status");
        strategyFactory = new HotelStrategyFactory(List.of(strategy));
        service = new HotelService(repository, strategyFactory);
    }

    // ── findAll ────────────────────────────────────────────────────────────────

    @Test
    void should_return_all_responses_when_repository_has_entities() {
        Pageable pageable = PageRequest.of(0, 20);
        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(fixture)));
        Page<HotelResponse> result = service.findAll(new HotelFilterRequest(), pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    // ── findById ───────────────────────────────────────────────────────────────

    @Test
    void should_return_response_when_entity_exists_by_id() {
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));

        HotelResponse result = service.findById(EXISTING_ID);

        assertThat(result.id()).isEqualTo(EXISTING_ID);
        verify(repository).findById(EXISTING_ID);
    }

    @Test
    void should_throw_entity_not_found_when_id_is_unknown() {
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(MISSING_ID))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));
    }

    // ── create ─────────────────────────────────────────────────────────────────

    @Test
    void should_persist_entity_and_return_response_when_create_is_called() {
        HotelCreateRequest request = new HotelCreateRequest("test-value", "test-value", "test-value", "test-value", 1, "test-value", "test-value", "test-value");
        when(strategy.supports(any())).thenReturn(true);
        when(repository.save(any(Hotel.class))).thenReturn(fixture);

        HotelResponse result = service.create(request);

        assertThat(result.id()).isEqualTo(fixture.getId());
        assertThat(result.name()).isEqualTo(fixture.getName());
        
        ArgumentCaptor<Hotel> createCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(repository).save(createCaptor.capture());
        assertThat(createCaptor.getValue().getName()).isEqualTo(request.name());
        
    }

    // ── update ─────────────────────────────────────────────────────────────────

    @Test
    void should_update_entity_and_return_response_when_entity_exists() {
        HotelUpdateRequest request = new HotelUpdateRequest("updated-value", "updated-value", "updated-value", "updated-value", 2, "updated-value", "updated-value", "updated-value");
        when(repository.findById(EXISTING_ID)).thenReturn(Optional.of(fixture));
        when(repository.save(any(Hotel.class))).thenReturn(fixture);

        HotelResponse result = service.update(EXISTING_ID, request);

        assertThat(result.id()).isEqualTo(EXISTING_ID);
        assertThat(result.name()).isEqualTo(fixture.getName());
        verify(repository).findById(EXISTING_ID);
        ArgumentCaptor<Hotel> updateCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(repository).save(updateCaptor.capture());
        assertThat(updateCaptor.getValue().getName()).isEqualTo(request.name());
        
    }

    @Test
    void should_throw_entity_not_found_when_updating_with_unknown_id() {
        HotelUpdateRequest request = new HotelUpdateRequest("updated-value", "updated-value", "updated-value", "updated-value", 2, "updated-value", "updated-value", "updated-value");
        when(repository.findById(MISSING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(MISSING_ID, request))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));

        verify(repository).findById(MISSING_ID);
        verify(repository, never()).save(any(Hotel.class));
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
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining(String.valueOf(MISSING_ID));

        verify(repository, never()).delete(any(Hotel.class));
    }
}
