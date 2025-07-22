package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockJpaRepository extends JpaRepository<Coupon,Long> {
    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}
