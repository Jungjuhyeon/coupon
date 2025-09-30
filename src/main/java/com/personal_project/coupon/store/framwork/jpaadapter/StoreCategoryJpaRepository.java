package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.domain.model.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryJpaRepository extends JpaRepository<StoreCategory,Long> {
}
