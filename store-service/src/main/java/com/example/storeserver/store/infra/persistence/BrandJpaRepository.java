package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<Brand,Long> {
}
