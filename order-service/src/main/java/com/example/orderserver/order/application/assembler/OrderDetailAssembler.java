package com.example.orderserver.order.application.assembler;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.framework.web.response.OrderInfoOutPutDTO;
import com.example.orderserver.order.framework.web.response.OrderMenuOutputDTO;
import com.example.orderserver.order.infra.store.dto.response.MenuOrderViewFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderDetailAssembler {
    public OrderInfoOutPutDTO assemble(Order order, StoreOrderViewFeignDTO storeOrderView) {
        Map<Long, String> menuNameMap =
                storeOrderView.getMenuOrderViewFeignDTOList().stream()
                        .collect(Collectors.toMap(
                                MenuOrderViewFeignDTO::getMenuId,
                                MenuOrderViewFeignDTO::getName
                        ));

        List<OrderMenuOutputDTO> orderMenus =
                order.getOrderMenuList().stream()
                        .map(orderMenu -> OrderMenuOutputDTO.mapToDTO(
                                orderMenu.getId(),
                                orderMenu.getMenuId(),
                                menuNameMap.get(orderMenu.getMenuId()),
                                orderMenu.getPrice(),
                                orderMenu.getQuantity(),
                                orderMenu.getTotalPrice()
                        ))
                        .toList();

        return OrderInfoOutPutDTO.mapToDTO(
                order,
                storeOrderView.getCategoryName(),
                storeOrderView.getBrandName(),
                storeOrderView.getStoreName(),
                orderMenus
        );
    }
}