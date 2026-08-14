package com.synth.hotelbookingmanagement.hotel;

import com.synth.hotelbookingmanagement.exception.HotelNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class HotelService {

    private final HotelRepository repository;
    private final HotelStrategyFactory strategyFactory;
    public Page<HotelResponse> findAll(HotelFilterRequest filter, Pageable pageable) {
        return repository.findAll(HotelSpecification.build(filter), pageable)
                .map(HotelResponse::from);
    }
    public HotelResponse findById(UUID id) {
        Hotel entity = repository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return HotelResponse.from(entity);
    }
    public HotelResponse create(HotelCreateRequest request) {
        Hotel entity = HotelMapper.toEntity(request);
        
        
        
        
        
        
        
        
        
        strategyFactory.resolve(entity.getStatus()).execute(entity);
        HotelResponse response = HotelResponse.from(repository.save(entity));
        return response;
    }
    public HotelResponse update(UUID id, HotelUpdateRequest request) {
        Hotel entity = repository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        HotelMapper.updateEntity(entity, request);
        
        
        
        
        
        
        
        
        
        HotelResponse response = HotelResponse.from(repository.save(entity));
        return response;
    }
    public void delete(UUID id) {
        Hotel entity = repository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        repository.delete(entity);
    }

}
