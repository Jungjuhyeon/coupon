package com.example.orderserver.order.application.outputport;

import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;

import java.util.List;

public interface StoreOutputPort {
    void validateStore(Long storeId);
    List<MenuInfoFeignDTO> getMenuInfoList(List<Long> couponIssueIdList);

    // 조회 (fallback 허용)
    StoreOrderViewFeignDTO getStoreOrderView(Long storeId, List<Long> menuIds);

    // 이벤트 (fallback 금지)
    StoreOrderViewFeignDTO getStoreOrderViewForProjection(Long storeId, List<Long> menuIds);
}
