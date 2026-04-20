package com.example.storeserver.store.application.usecase;


import com.example.storeserver.store.framework.web.response.StoreInfoOutputDTO;
import com.example.storeserver.store.framework.web.response.StoreOrderViewFeignDTO;

import java.util.List;

public interface InquiryStoreUseCase {

    StoreInfoOutputDTO getStore(Long storeId);
    StoreOrderViewFeignDTO getStoreOrderView(Long storeId, List<Long> menuIds);
    boolean existsById(Long storeId);
}
