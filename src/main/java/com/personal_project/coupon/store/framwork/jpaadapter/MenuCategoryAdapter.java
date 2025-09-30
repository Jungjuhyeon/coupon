package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.application.outputport.MenuCategoryOutputPort;
import com.personal_project.coupon.store.domain.model.MenuCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MenuCategoryAdapter implements MenuCategoryOutputPort {

    private final MenuCategoryJpaRepository menuCategoryJpaRepository;

    @Override
    public Optional<MenuCategory> findById(Long menuCategoryId){
        return menuCategoryJpaRepository.findById(menuCategoryId);
    }

}
