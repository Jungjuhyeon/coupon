package com.example.orderserver.order.infra.assembler;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
import com.example.orderserver.order.infra.store.dto.response.MenuOrderViewFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderSummaryAssembler {
    public OrderSummaryDocument assemble(Order order, Long memberId, StoreOrderViewFeignDTO storeView) {

        Map<Long, String> menuNameMap = storeView.getMenuOrderViewFeignDTOList().stream()
                .collect(Collectors.toMap
                        (MenuOrderViewFeignDTO::getMenuId, MenuOrderViewFeignDTO::getName));

        List<OrderSummaryDocument.OrderMenuDocument> menuList =
                order.getOrderMenuList().stream()
                        .map(om -> toMenuDocument(om, menuNameMap))
                        .toList();

        return OrderSummaryDocument.from(
                order,
                memberId,
                storeView.getCategoryName(),
                storeView.getBrandName(),
                storeView.getStoreName(),
                menuList
        );
    }

    private OrderSummaryDocument.OrderMenuDocument toMenuDocument(
            OrderMenu orderMenu,
            Map<Long, String> menuNameMap) {

        return OrderSummaryDocument.OrderMenuDocument.mapToDoc(
                orderMenu.getId(),
                orderMenu.getMenuId(),
                menuNameMap.get(orderMenu.getMenuId()),
                orderMenu.getPrice(),
                orderMenu.getQuantity(),
                orderMenu.getTotalPrice());
    }
}