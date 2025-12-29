package com.example.storeserver.store.infra.persistence;

import com.example.storeserver.store.application.outputport.MenuOutputPort;
import com.example.storeserver.store.domain.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuAdapter implements MenuOutputPort {

    private final MenuJpaRepository menuJpaRepository;
    @Override
    public List<Menu> saveAll(List<Menu> menuList){
        return menuJpaRepository.saveAll(menuList);
    }

    @Override
    public List<Menu> findAllById(List<Long> menuIdList){
        return menuJpaRepository.findAllById(menuIdList);
    }

}
