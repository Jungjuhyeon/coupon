package com.personal_project.coupon.store.framwork.jpaadapter;

import com.personal_project.coupon.store.domain.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<Store,Long> {
}
