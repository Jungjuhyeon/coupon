package com.example.storeserver.store.application.outputport;


import com.example.storeserver.store.domain.model.Brand;

import java.util.Optional;

public interface BrandOutputPort {

    public Optional<Brand> findById(Long brandId);
}
