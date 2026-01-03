package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.application.outputport.StoreCategoryOutputPort;
import com.example.storeserver.store.domain.model.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreCategoryAdapter implements StoreCategoryOutputPort {

    private final StoreCategoryJpaRepository storeCategoryJpaRepository;

    @Override
    public Optional<StoreCategory> findById(Long storeId){
        return storeCategoryJpaRepository.findById(storeId);
    }

}
