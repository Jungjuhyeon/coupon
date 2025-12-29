package com.example.storeserver.store.infra.persistence;


import com.example.storeserver.store.application.dto.StoreBasicInfo;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.domain.model.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreAdapter implements StoreOutputPort {

    private final StoreJpaRepository storeJpaRepository;

    @Override
    public Store save(Store store){
        return storeJpaRepository.save(store);
    }

    @Override
    public Optional<Store> findById(Long storeId){
        return storeJpaRepository.findById(storeId);
    }

    @Override
    public Optional<StoreBasicInfo> findStoreBasicInfo(Long storeId){
        return storeJpaRepository.findStoreBasicInfo(storeId);
    }

    @Override
    public boolean existsById(Long storeId) {
        return storeJpaRepository.existsById(storeId);
    }

}
