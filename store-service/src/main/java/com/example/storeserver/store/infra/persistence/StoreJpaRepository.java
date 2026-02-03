package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.application.dto.StoreBasicInfo;
import com.example.storeserver.store.domain.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreJpaRepository extends JpaRepository<Store,Long> {
    @Query("""
    select new com.example.storeserver.store.application.dto.StoreBasicInfo
    (
        s.id,
        s.name,
        b.name,
        c.name
    )
    from  Store s
    join  s.brand b
    join  s.storeCategory c
    where s.id = :storeId
    """)
    Optional<StoreBasicInfo> findStoreBasicInfo(@Param("storeId") Long storeId);
}
