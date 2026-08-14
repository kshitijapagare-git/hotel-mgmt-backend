package com.synth.hotelbookingmanagement.room;

import com.synth.hotelbookingmanagement.exception.RoomNotFoundException;
import com.synth.hotelbookingmanagement.exception.HotelNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.synth.hotelbookingmanagement.hotel.Hotel;
import com.synth.hotelbookingmanagement.hotel.HotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository repository;
    private final HotelRepository hotelRepository;
    private final RoomStrategyFactory strategyFactory;
    @Transactional(readOnly = true)
    public Page<RoomResponse> findAll(RoomFilterRequest filter, Pageable pageable) {
        return repository.findAll(RoomSpecification.build(filter), pageable)
                .map(RoomResponse::from);
    }
    @Transactional(readOnly = true)
    public RoomResponse findById(UUID id) {
        Room entity = repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        return RoomResponse.from(entity);
    }
    @Transactional
    public RoomResponse create(RoomCreateRequest request) {
        Room entity = RoomMapper.toEntity(request);
        
        
        
        
        
        
        
        entity.setHotelId(hotelRepository.findById(request.hotelId())
                .orElseThrow(() -> new HotelNotFoundException(request.hotelId())));
        
        strategyFactory.resolve(entity.getType()).execute(entity);
        RoomResponse response = RoomResponse.from(repository.save(entity));
        return response;
    }
    @Transactional
    public RoomResponse update(UUID id, RoomUpdateRequest request) {
        Room entity = repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        RoomMapper.updateEntity(entity, request);
        
        
        
        
        
        
        
        if (request.hotelId() != null) {
            entity.setHotelId(hotelRepository.findById(request.hotelId())
                    .orElseThrow(() -> new HotelNotFoundException(request.hotelId())));
        }
        
        RoomResponse response = RoomResponse.from(repository.save(entity));
        return response;
    }
    @Transactional
    public void delete(UUID id) {
        Room entity = repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        repository.delete(entity);
    }

}
