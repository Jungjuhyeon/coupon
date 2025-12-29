package com.example.storeserver.store.framwork.web;

import com.example.storeserver.store.application.usecase.InquiryStoreUseCase;
import com.example.storeserver.store.framwork.web.response.StoreOrderViewFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/stores")
public class StoreInternalController {
    private final InquiryStoreUseCase inquiryStoreUseCase;

    @GetMapping("/{storeId}")
    public boolean existsById(@PathVariable Long storeId){
        return inquiryStoreUseCase.existsById(storeId);
    }

    @GetMapping("/{storeId}/order-view")
    public StoreOrderViewFeignDTO getStoreOrderView(@PathVariable Long storeId, @RequestParam List<Long> menuIds){
        return inquiryStoreUseCase.getStoreOrderView(storeId,menuIds);
    }
}
