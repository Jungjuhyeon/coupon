package com.example.storeserver.store.framwork.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.storeserver.store.application.usecase.AddMenuUseCase;
import com.example.storeserver.store.framwork.web.request.MenuListDTO;
import com.example.storeserver.store.framwork.web.response.MenuOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu")
public class MenuController {
    private final AddMenuUseCase addMenuUseCase;

    @PostMapping("/create")
    public SuccessResponse<MenuOutputDTO> create(@RequestBody MenuListDTO request){
        MenuOutputDTO response = addMenuUseCase.create(request);
        return SuccessResponse.success(response);
    }
}
