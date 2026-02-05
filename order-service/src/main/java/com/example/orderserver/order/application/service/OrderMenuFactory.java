package com.example.orderserver.order.application.service;

import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.framwork.web.request.OrderMenuInfoDTO;
import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMenuFactory {
    private final StoreOutputPort storeOutputPort;

    public List<OrderMenu> createOrderMenus(Order order, List<OrderMenuInfoDTO> menuInfoList) {
        List<Long> menuIds = menuInfoList.stream()
                .map(OrderMenuInfoDTO::getMenuId)
                .toList();

        Map<Long, Integer> menuQuantityMap = menuInfoList.stream()
                .collect(Collectors.toMap(
                        OrderMenuInfoDTO::getMenuId,
                        OrderMenuInfoDTO::getCount)
                );

        List<MenuInfoFeignDTO> menuInfos = storeOutputPort.getMenuInfoList(menuIds);

        return menuInfos.stream()
                .map(menuInfo ->
                        OrderMenu.create(
                                order,
                                menuInfo.getMenuId(),
                                menuInfo.getPrice(),
                                menuQuantityMap.get(menuInfo.getMenuId())
                        )
                )
                .toList();
    }
}
