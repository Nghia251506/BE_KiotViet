package com.example.be_kiotviet.service.Shop;

import com.example.be_kiotviet.dto.ShopRequest;
import com.example.be_kiotviet.dto.ShopResponse;
import com.example.be_kiotviet.entity.Shops;
import com.example.be_kiotviet.exception.ResourceNotFoundException;
import com.example.be_kiotviet.mapper.ShopMapper;
import com.example.be_kiotviet.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository repository;
    private final ShopMapper mapper;

    @Override
    public ShopResponse create(ShopRequest request) {
        if (repository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại!");
        }
        Shops entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public ShopResponse update(Long id, ShopRequest request) {
        Shops entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop không tồn tại"));
        mapper.updateEntity(entity, request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public ShopResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Shop không tồn tại"));
    }

    @Override
    public Page<ShopResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Shop không tồn tại");
        }
        repository.deleteById(id);
    }
}
