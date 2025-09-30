package com.personal_project.coupon.store.application.outputport;

import com.personal_project.coupon.store.domain.model.StoreCategory;

import java.util.Optional;

public interface StoreCategoryOutputPort {
    public Optional<StoreCategory> findById(Long storeCategoryId);
}
