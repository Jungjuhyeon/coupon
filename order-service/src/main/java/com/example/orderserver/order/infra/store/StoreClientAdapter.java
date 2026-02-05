package com.example.orderserver.order.infra.store;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.exception.OrderErrorCode;
import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreClientAdapter implements StoreOutputPort {
    private final StoreFeignClient storeFeignClient;
    @Override
    @CircuitBreaker(
            name = "store-service",
            fallbackMethod = "validateStoreFallback"
    )
    public void validateStore(Long storeId){
        storeFeignClient.validateStore(storeId);
    }
    private void validateStoreFallback(Long storeId, Throwable throwable) {
        if (throwable instanceof FeignException feignEx && feignEx.status() == 404) {
            throw new BusinessException(OrderErrorCode.STORE_NOT_FOUND);
        }
        throw new BusinessException(OrderErrorCode.STORE_SERVICE_UNAVAILABLE);
    }
    @Override
    @CircuitBreaker(
            name = "store-service",
            fallbackMethod = "storeFallback"
    )
    public List<MenuInfoFeignDTO> getMenuInfoList(List<Long> couponIssueIdList){
        return storeFeignClient.getMenus(couponIssueIdList);
    }
    private List<MenuInfoFeignDTO> storeFallback(List<Long> couponIssueIdList, Throwable throwable) {
        if (throwable instanceof FeignException feignEx && feignEx.status() == 404) {
            throw new BusinessException(OrderErrorCode.MENU_NOT_FOUND);
        }
        throw new BusinessException(OrderErrorCode.STORE_SERVICE_UNAVAILABLE);
    }

    @Override
    @CircuitBreaker(
            name = "store-service",
            fallbackMethod = "getStoreOrderViewFallback"
    )
    public StoreOrderViewFeignDTO getStoreOrderView(Long storeId, List<Long> menuIds){
        return storeFeignClient.getStoreOrderView(storeId,menuIds);
    }
    private StoreOrderViewFeignDTO getStoreOrderViewFallback(Long storeId, List<Long> menuIds, Throwable throwable) {
        if (throwable instanceof FeignException feignEx && feignEx.status() == 404) {
            throw new BusinessException(OrderErrorCode.ORDER_VIEW_RESOURCE_NOT_FOUND);
        }
        return new StoreOrderViewFeignDTO("알 수 없음", null, null, List.of());
    }

    @Override
    public StoreOrderViewFeignDTO getRequiredStoreOrderView(Long storeId, List<Long> menuIds) {
        try{
            return storeFeignClient.getStoreOrderView(storeId,menuIds);
        } catch (FeignException e) {
            throw new BusinessException(OrderErrorCode.ORDER_VIEW_RESOURCE_NOT_FOUND);
        }
    }

}
