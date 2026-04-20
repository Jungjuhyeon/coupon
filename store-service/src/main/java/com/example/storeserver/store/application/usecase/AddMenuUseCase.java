package com.example.storeserver.store.application.usecase;

import com.example.storeserver.store.framework.web.request.MenuListDTO;
import com.example.storeserver.store.framework.web.response.MenuOutputDTO;

public interface AddMenuUseCase {
    MenuOutputDTO create(MenuListDTO request);
}
