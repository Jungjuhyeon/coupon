package com.personal_project.coupon.store.framwork.web;

import com.personal_project.coupon.global.exception.response.SuccessResponse;
import com.personal_project.coupon.global.util.jwt.CustomUserDetails;
import com.personal_project.coupon.store.application.usecase.AddStoreUsecase;
import com.personal_project.coupon.store.application.usecase.InquiryStoreUsecase;
import com.personal_project.coupon.store.framwork.web.request.StoreInfoDTO;
import com.personal_project.coupon.store.framwork.web.response.StoreInfoOutputDTO;
import com.personal_project.coupon.store.framwork.web.response.StoreOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {

    private final AddStoreUsecase addStoreUsecase;
    private final InquiryStoreUsecase inquiryStoreUsecase;


    @PostMapping("/create")
    public SuccessResponse<StoreOutputDTO> create(@AuthenticationPrincipal CustomUserDetails member,
                                                  @RequestBody StoreInfoDTO request){
        Long memberId = member.getMemberId();

        StoreOutputDTO response = addStoreUsecase.create(memberId,request);
        return SuccessResponse.success(response);
    }

    @GetMapping("/{storeId}")
    public SuccessResponse<StoreInfoOutputDTO> getStore(@PathVariable Long storeId){
        StoreInfoOutputDTO respone = inquiryStoreUsecase.getStore(storeId);

        return SuccessResponse.success(respone);
    }

}
