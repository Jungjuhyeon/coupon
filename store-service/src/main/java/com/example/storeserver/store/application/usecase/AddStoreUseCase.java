package com.example.storeserver.store.application.usecase;

import com.example.storeserver.store.framwork.web.request.StoreInfoDTO;
import com.example.storeserver.store.framwork.web.response.StoreIdOutputDTO;

public interface AddStoreUseCase {
    StoreIdOutputDTO create(Long memberId, StoreInfoDTO request);
}
