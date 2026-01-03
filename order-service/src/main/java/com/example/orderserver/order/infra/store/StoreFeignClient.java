package com.example.orderserver.order.infra.store;

import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "store-service", url = "${store.service.url}")
public interface StoreFeignClient {

    @GetMapping("/internal/stores/{storeId}")
    void validateStore(@PathVariable Long storeId);
    @GetMapping("/internal/menus")
    List<MenuInfoFeignDTO> getMenus(@RequestParam("menuIds") List<Long> menuIds);
    @GetMapping("/internal/stores/{storeId}/order-view")
    @Bulkhead(name = "order-circuit-breaker")
    StoreOrderViewFeignDTO getStoreOrderView(@PathVariable Long storeId, @RequestParam List<Long> menuIds);

}
