package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<Brand,Long> {
}
