package com.example.storeserver.store.application.usecase;

import com.example.storeserver.store.framwork.web.request.MenuListDTO;
import com.example.storeserver.store.framwork.web.response.MenuOutputDTO;

public interface AddMenuUseCase {
    MenuOutputDTO create(MenuListDTO request);
}
