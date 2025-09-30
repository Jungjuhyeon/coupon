package com.personal_project.coupon.store.application.inputport;

import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.store.application.outputport.BrandOutputPort;
import com.personal_project.coupon.store.application.outputport.StoreCategoryOutputPort;
import com.personal_project.coupon.store.application.outputport.StoreOutputPort;
import com.personal_project.coupon.store.application.usecase.AddStoreUsecase;
import com.personal_project.coupon.store.domain.model.Brand;
import com.personal_project.coupon.store.domain.model.Store;
import com.personal_project.coupon.store.domain.model.StoreCategory;
import com.personal_project.coupon.store.framwork.web.request.StoreInfoDTO;
import com.personal_project.coupon.store.framwork.web.response.StoreOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddStoreInputPort implements AddStoreUsecase {

    private final StoreCategoryOutputPort storeCategoryOutputPort;
    private final BrandOutputPort brandOutputPort;
    private final MemberOutputPort memberOutputPort;
    private final StoreOutputPort storeOutputPort;

    @Override
    @Transactional
    public StoreOutputDTO create(Long memberId, StoreInfoDTO request){

        StoreCategory storeCategory = storeCategoryOutputPort.findById(request.getStoreCategoryId())
                .orElseThrow(()->new BusinessException(CommonErrorCode.STORE_CATEGORY_NOT_FOUND));

        Brand brand = brandOutputPort.findById(request.getBrandId())
                .orElseThrow(() -> new BusinessException(CommonErrorCode.BRAND_NOT_FOUND));

        Member owner = memberOutputPort.findById(memberId)
                .orElseThrow(()-> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

        Store newStore = Store.create(brand,storeCategory,owner,request.getName(),request.getPhone(),request.getAddress());

        return StoreOutputDTO.mapToDTO(storeOutputPort.save(newStore));
    }

}
