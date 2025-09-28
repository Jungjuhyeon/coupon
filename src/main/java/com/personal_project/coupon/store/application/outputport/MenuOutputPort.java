package com.personal_project.coupon.store.application.outputport;

import com.personal_project.coupon.store.domain.model.Menu;

import java.util.List;

public interface MenuOutputPort {

    public List<Menu> saveAll(List<Menu> menuList);

    List<Menu> findAllById(List<Long> menuIdList);

}
