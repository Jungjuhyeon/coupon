package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.domain.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<Menu,Long> {
}
