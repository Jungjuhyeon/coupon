package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.application.outputport.BrandOutputPort;
import com.example.storeserver.store.domain.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class BrandAdapter implements BrandOutputPort {

    private final BrandJpaRepository brandJpaRepository;

    public Optional<Brand> findById(Long brandId){
        return brandJpaRepository.findById(brandId);
    }
}
