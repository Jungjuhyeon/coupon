package com.example.storeserver.store.framework.web;

import com.example.storeserver.store.application.usecase.InquiryMenuUseCase;
import com.example.storeserver.store.framework.web.response.MenuInfoFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/menus")
public class MenuInternalController {
    private final InquiryMenuUseCase inquiryMenuUseCase;

    @GetMapping
    public List<MenuInfoFeignDTO> getMenus(@RequestParam List<Long> menuIds){
        return inquiryMenuUseCase.getMenuIdAndPrice(menuIds);
    }
}
