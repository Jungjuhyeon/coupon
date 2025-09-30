package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.domain.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryJpaRepository extends JpaRepository<MenuCategory,Long> {
}
