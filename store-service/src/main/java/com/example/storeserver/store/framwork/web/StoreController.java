package com.example.storeserver.store.framwork.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.common.global.security.AuthPrincipal;
import com.example.storeserver.store.application.usecase.AddStoreUseCase;
import com.example.storeserver.store.application.usecase.InquiryStoreUseCase;
import com.example.storeserver.store.framwork.web.request.StoreInfoDTO;
import com.example.storeserver.store.framwork.web.response.StoreInfoOutputDTO;
import com.example.storeserver.store.framwork.web.response.StoreIdOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {
    private final AddStoreUseCase addStoreUsecase;
    private final InquiryStoreUseCase inquiryStoreUsecase;

    @PostMapping("/create")
    public SuccessResponse<StoreIdOutputDTO> create(@AuthenticationPrincipal AuthPrincipal principal,
                                                    @RequestBody StoreInfoDTO request){
        Long memberId = principal.getId();
        StoreIdOutputDTO response = addStoreUsecase.create(memberId,request);
        return SuccessResponse.success(response);
    }

    @GetMapping("/{storeId}")
    public SuccessResponse<StoreInfoOutputDTO> getStore(@PathVariable Long storeId){
        StoreInfoOutputDTO respone = inquiryStoreUsecase.getStore(storeId);
        return SuccessResponse.success(respone);
    }

}
