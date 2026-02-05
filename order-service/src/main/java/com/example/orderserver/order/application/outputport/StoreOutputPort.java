package com.example.orderserver.order.application.outputport;

import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;

import java.util.List;

public interface StoreOutputPort {
    void validateStore(Long storeId);
    List<MenuInfoFeignDTO> getMenuInfoList(List<Long> couponIssueIdList);
    StoreOrderViewFeignDTO getStoreOrderView(Long storeId, List<Long> menuIds);
    StoreOrderViewFeignDTO getRequiredStoreOrderView(Long storeId, List<Long> menuIds);
}
