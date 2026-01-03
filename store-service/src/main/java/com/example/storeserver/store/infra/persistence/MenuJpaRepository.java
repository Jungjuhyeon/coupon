package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.domain.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuJpaRepository extends JpaRepository<Menu,Long> {
}
