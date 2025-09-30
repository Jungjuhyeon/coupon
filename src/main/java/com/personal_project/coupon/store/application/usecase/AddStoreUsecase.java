package com.personal_project.coupon.store.application.usecase;

import com.personal_project.coupon.store.framwork.web.request.StoreInfoDTO;
import com.personal_project.coupon.store.framwork.web.response.StoreOutputDTO;

public interface AddStoreUsecase {
    public StoreOutputDTO create(Long memberId, StoreInfoDTO request);
}
