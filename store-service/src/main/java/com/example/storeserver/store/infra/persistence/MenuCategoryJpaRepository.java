package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.domain.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryJpaRepository extends JpaRepository<MenuCategory,Long> {
}
