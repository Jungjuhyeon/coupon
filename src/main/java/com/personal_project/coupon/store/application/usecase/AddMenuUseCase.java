package com.personal_project.coupon.store.application.usecase;

import com.personal_project.coupon.store.framwork.web.request.MenuListDTO;
import com.personal_project.coupon.store.framwork.web.response.MenuOutputDTO;

public interface AddMenuUseCase {
    public MenuOutputDTO create(MenuListDTO request);
}
