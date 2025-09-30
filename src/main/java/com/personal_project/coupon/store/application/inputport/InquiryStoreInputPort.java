package com.personal_project.coupon.store.application.inputport;

import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.store.application.outputport.StoreOutputPort;
import com.personal_project.coupon.store.application.usecase.InquiryStoreUsecase;
import com.personal_project.coupon.store.domain.model.Store;
import com.personal_project.coupon.store.framwork.web.response.StoreInfoOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryStoreInputPort implements InquiryStoreUsecase {
    private final StoreOutputPort storeOutputPort;

    @Override
    public StoreInfoOutputDTO getStore(Long storeId){
        Store store = storeOutputPort.findById(storeId).
                orElseThrow(()-> new BusinessException(CommonErrorCode.STORE_NOT_FOUND));

        return StoreInfoOutputDTO.mapToDTO(store);
    }


}
