package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.domain.model.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory,Long> {
}
