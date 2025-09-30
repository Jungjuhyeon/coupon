package com.personal_project.coupon.store.application.usecase;

import com.personal_project.coupon.store.framwork.web.response.StoreInfoOutputDTO;

public interface InquiryStoreUsecase {

    public StoreInfoOutputDTO getStore(Long storeId);

}
