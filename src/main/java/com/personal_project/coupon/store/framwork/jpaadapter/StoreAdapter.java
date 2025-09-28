package com.personal_project.coupon.store.framwork.jpaadapter;


import com.personal_project.coupon.store.application.outputport.StoreOutputPort;
import com.personal_project.coupon.store.domain.model.Store;
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

}
