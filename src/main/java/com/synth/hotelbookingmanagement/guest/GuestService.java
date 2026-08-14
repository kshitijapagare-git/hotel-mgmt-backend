package com.synth.hotelbookingmanagement.guest;

import com.synth.hotelbookingmanagement.exception.GuestNotFoundException;
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
public class GuestService {

    private final GuestRepository repository;
    public Page<GuestResponse> findAll(GuestFilterRequest filter, Pageable pageable) {
        return repository.findAll(GuestSpecification.build(filter), pageable)
                .map(GuestResponse::from);
    }
    public GuestResponse findById(UUID id) {
        Guest entity = repository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
        return GuestResponse.from(entity);
    }
    public GuestResponse create(GuestCreateRequest request) {
        Guest entity = GuestMapper.toEntity(request);
        
        
        
        
        
        
        
        GuestResponse response = GuestResponse.from(repository.save(entity));
        return response;
    }
    public GuestResponse update(UUID id, GuestUpdateRequest request) {
        Guest entity = repository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
        GuestMapper.updateEntity(entity, request);
        
        
        
        
        
        
        
        GuestResponse response = GuestResponse.from(repository.save(entity));
        return response;
    }
    public void delete(UUID id) {
        Guest entity = repository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));
        repository.delete(entity);
    }

}
