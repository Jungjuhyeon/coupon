package com.example.storeserver.store.application.outputport;


import com.example.storeserver.store.application.dto.StoreBasicInfo;
import com.example.storeserver.store.domain.model.Store;

import java.util.Optional;

public interface StoreOutputPort {

    public Store save(Store store);

    public Optional<Store> findById(Long storeId);

    Optional<StoreBasicInfo> findStoreBasicInfo(Long storeId);

    boolean existsById(Long storeId);
}

