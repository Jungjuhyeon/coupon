package com.example.storeserver.store.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.dto.StoreBasicInfo;
import com.example.storeserver.store.application.outputport.MemberOutputPort;
import com.example.storeserver.store.application.outputport.MenuOutputPort;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.application.usecase.InquiryStoreUseCase;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.framwork.web.response.MenuOrderViewFeignDTO;
import com.example.storeserver.store.application.dto.OwnerInfo;
import com.example.storeserver.store.framwork.web.response.StoreInfoOutputDTO;
import com.example.storeserver.store.framwork.web.response.StoreOrderViewFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryStoreInputPort implements InquiryStoreUseCase {
    private final StoreOutputPort storeOutputPort;
    private final MenuOutputPort menuOutputPort;
    private final MemberOutputPort memberOutputPort;

    @Override
    public StoreInfoOutputDTO getStore(Long storeId){
        Store store = storeOutputPort.findById(storeId).
                orElseThrow(()-> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));

        OwnerInfo ownerInfo = OwnerInfo.mapToDTO(memberOutputPort.getOwnerInfo(store.getOwnerId()));

        return StoreInfoOutputDTO.mapToDTO(store,ownerInfo);
    }

    @Override
    public StoreOrderViewFeignDTO getStoreOrderView(Long storeId, List<Long> menuIds){
        StoreBasicInfo storeInfo = storeOutputPort.findStoreBasicInfo(storeId)
                        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));

        List<MenuOrderViewFeignDTO> menuOrderViewFeignDTOList = menuOutputPort.findAllById(menuIds).stream()
                .map(m-> MenuOrderViewFeignDTO.mapToDTO(m.getId(),m.getName()))
                .toList();

        return StoreOrderViewFeignDTO.mapToDTO(storeInfo.getStoreName(),storeInfo.getBrandName(),storeInfo.getCategoryName(), menuOrderViewFeignDTOList);
    }

    @Override
    public boolean existsById(Long storeId){
        return storeOutputPort.existsById(storeId);
    }

}
