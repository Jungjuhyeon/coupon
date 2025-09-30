package com.personal_project.coupon.store.application.outputport;

import com.personal_project.coupon.store.domain.model.Brand;

import java.util.Optional;

public interface BrandOutputPort {

    public Optional<Brand> findById(Long brandId);
}
