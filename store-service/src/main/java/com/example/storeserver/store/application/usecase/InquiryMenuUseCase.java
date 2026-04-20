package com.example.storeserver.store.application.usecase;

import com.example.storeserver.store.framework.web.response.MenuInfoFeignDTO;

import java.util.List;

public interface InquiryMenuUseCase {
    List<MenuInfoFeignDTO> getMenuIdAndPrice(List<Long> menuIds);
}
