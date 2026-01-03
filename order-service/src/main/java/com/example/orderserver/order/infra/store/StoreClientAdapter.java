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
    public void validateStore(Long storeId){
        try{
            storeFeignClient.validateStore(storeId);
        } catch (FeignException e) {
            throw new BusinessException(OrderErrorCode.ORDER_PRECONDITION_FAILED);
        }
    }
    @Override
    public List<MenuInfoFeignDTO> getMenuInfoList(List<Long>couponIssueIdList){
        try{
            return storeFeignClient.getMenus(couponIssueIdList);
        } catch (FeignException e) {
            throw new BusinessException(OrderErrorCode.ORDER_PRECONDITION_FAILED);
        }
    }
    @Override
    @CircuitBreaker(
            name = "order-circuit-breaker",
            fallbackMethod = "getStoreOrderViewFallback"
    )
    public StoreOrderViewFeignDTO getStoreOrderView(Long storeId, List<Long> menuIds){
        try{
            return storeFeignClient.getStoreOrderView(storeId,menuIds);
        } catch (FeignException e) {
            throw new BusinessException(OrderErrorCode.ORDER_VIEW_RESOURCE_NOT_FOUND);
        }
    }
    private StoreOrderViewFeignDTO getStoreOrderViewFallback(Long storeId, List<Long> menuIds, Throwable t) {
        return new StoreOrderViewFeignDTO("알 수 없음", null, null, List.of());
    }

    @Override
    public StoreOrderViewFeignDTO getStoreOrderViewForProjection(Long storeId, List<Long> menuIds) {
        try{
            return storeFeignClient.getStoreOrderView(storeId,menuIds);
        } catch (FeignException e) {
            throw new BusinessException(OrderErrorCode.ORDER_VIEW_RESOURCE_NOT_FOUND);
        }
    }

}
