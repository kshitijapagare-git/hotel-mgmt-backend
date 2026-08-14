package com.synth.hotelbookingmanagement.reservation;

import com.synth.hotelbookingmanagement.exception.ReservationNotFoundException;
import com.synth.hotelbookingmanagement.exception.RoomNotFoundException;
import com.synth.hotelbookingmanagement.exception.GuestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.synth.hotelbookingmanagement.room.Room;
import com.synth.hotelbookingmanagement.room.RoomRepository;
import com.synth.hotelbookingmanagement.guest.Guest;
import com.synth.hotelbookingmanagement.guest.GuestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationStrategyFactory strategyFactory;
    @Transactional(readOnly = true)
    public Page<ReservationResponse> findAll(ReservationFilterRequest filter, Pageable pageable) {
        return repository.findAll(ReservationSpecification.build(filter), pageable)
                .map(ReservationResponse::from);
    }
    @Transactional(readOnly = true)
    public ReservationResponse findById(UUID id) {
        Reservation entity = repository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return ReservationResponse.from(entity);
    }
    @Transactional
    public ReservationResponse create(ReservationCreateRequest request) {
        Reservation entity = ReservationMapper.toEntity(request);
        
        
        
        
        
        
        entity.setRoomId(roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RoomNotFoundException(request.roomId())));
        
        entity.setGuestId(guestRepository.findById(request.guestId())
                .orElseThrow(() -> new GuestNotFoundException(request.guestId())));
        
        strategyFactory.resolve(entity.getStatus()).execute(entity);
        ReservationResponse response = ReservationResponse.from(repository.save(entity));
        return response;
    }
    @Transactional
    public ReservationResponse update(UUID id, ReservationUpdateRequest request) {
        Reservation entity = repository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        ReservationMapper.updateEntity(entity, request);
        
        
        
        
        
        
        if (request.roomId() != null) {
            entity.setRoomId(roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new RoomNotFoundException(request.roomId())));
        }
        
        if (request.guestId() != null) {
            entity.setGuestId(guestRepository.findById(request.guestId())
                    .orElseThrow(() -> new GuestNotFoundException(request.guestId())));
        }
        
        ReservationResponse response = ReservationResponse.from(repository.save(entity));
        return response;
    }
    @Transactional
    public void delete(UUID id) {
        Reservation entity = repository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        repository.delete(entity);
    }

}
