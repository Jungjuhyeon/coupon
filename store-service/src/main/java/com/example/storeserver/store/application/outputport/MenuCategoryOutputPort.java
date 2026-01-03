package com.example.storeserver.store.application.outputport;


import com.example.storeserver.store.domain.model.MenuCategory;

import java.util.Optional;

public interface MenuCategoryOutputPort {
    public Optional<MenuCategory> findById(Long menuCategoryId);
}
