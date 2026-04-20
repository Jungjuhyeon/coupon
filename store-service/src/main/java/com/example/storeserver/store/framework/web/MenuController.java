package com.example.storeserver.store.framework.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.storeserver.store.application.usecase.AddMenuUseCase;
import com.example.storeserver.store.framework.web.request.MenuListDTO;
import com.example.storeserver.store.framework.web.response.MenuOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {
    private final AddMenuUseCase addMenuUseCase;
    @PostMapping("/create")
    public SuccessResponse<MenuOutputDTO> create(@RequestBody MenuListDTO request){
        MenuOutputDTO response = addMenuUseCase.create(request);
        return SuccessResponse.success(response);
    }
}
