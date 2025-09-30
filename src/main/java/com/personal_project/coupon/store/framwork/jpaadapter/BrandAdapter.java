package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.application.outputport.BrandOutputPort;
import com.personal_project.coupon.store.domain.model.Brand;
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
