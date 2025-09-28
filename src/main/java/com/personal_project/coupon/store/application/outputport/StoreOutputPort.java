package com.personal_project.coupon.store.application.outputport;

import com.personal_project.coupon.store.domain.model.Store;

import java.util.Optional;

public interface StoreOutputPort {

    public Store save(Store store);

    public Optional<Store> findById(Long storeId);
}

