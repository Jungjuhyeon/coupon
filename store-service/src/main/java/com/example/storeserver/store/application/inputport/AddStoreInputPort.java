package com.example.storeserver.store.application.inputport;


import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.outputport.BrandOutputPort;
import com.example.storeserver.store.application.outputport.MemberOutputPort;
import com.example.storeserver.store.application.outputport.StoreCategoryOutputPort;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.application.usecase.AddStoreUseCase;
import com.example.storeserver.store.domain.model.Brand;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.domain.model.StoreCategory;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.framework.web.request.StoreInfoDTO;
import com.example.storeserver.store.framework.web.response.StoreIdOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddStoreInputPort implements AddStoreUseCase {

    private final StoreCategoryOutputPort storeCategoryOutputPort;
    private final BrandOutputPort brandOutputPort;
    private final MemberOutputPort memberOutputPort;
    private final StoreOutputPort storeOutputPort;

    @Override
    @Transactional
    public StoreIdOutputDTO create(Long memberId, StoreInfoDTO request){

        StoreCategory storeCategory = storeCategoryOutputPort.findById(request.getStoreCategoryId())
                .orElseThrow(()->new BusinessException(StoreErrorCode.STORE_CATEGORY_NOT_FOUND));

        Brand brand = brandOutputPort.findById(request.getBrandId())
                .orElseThrow(() -> new BusinessException(StoreErrorCode.BRAND_NOT_FOUND));

        boolean existsOwner = memberOutputPort.existsOwner(memberId);
        if (!existsOwner) {
            throw new BusinessException(StoreErrorCode.OWNER_NOT_FOUND);
        }

        Store newStore = Store.create(brand,storeCategory, memberId,request.getName(),request.getPhone(),request.getAddress());

        return StoreIdOutputDTO.mapToDTO(storeOutputPort.save(newStore));
    }

}
