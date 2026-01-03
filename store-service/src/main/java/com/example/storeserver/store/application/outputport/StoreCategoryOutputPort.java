package com.example.storeserver.store.application.outputport;


import com.example.storeserver.store.domain.model.StoreCategory;

import java.util.Optional;

public interface StoreCategoryOutputPort {
    public Optional<StoreCategory> findById(Long storeCategoryId);
}
