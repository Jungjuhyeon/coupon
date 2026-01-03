package com.example.storeserver.store.application.outputport;

import com.example.storeserver.store.domain.model.Menu;

import java.util.List;

public interface MenuOutputPort {

    public List<Menu> saveAll(List<Menu> menuList);

    List<Menu> findAllById(List<Long> menuIdList);

}
