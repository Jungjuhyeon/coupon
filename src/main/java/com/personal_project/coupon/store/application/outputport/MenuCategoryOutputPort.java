package com.personal_project.coupon.store.application.outputport;

import com.personal_project.coupon.store.domain.model.MenuCategory;

import java.util.Optional;

public interface MenuCategoryOutputPort {
    public Optional<MenuCategory> findById(Long menuCategoryId);
}
