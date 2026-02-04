package com.example.orderserver.order.infra.assembler;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.document.OrderReadModel;
import com.example.orderserver.order.infra.store.dto.response.MenuOrderViewFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderReadModelAssembler {
    public OrderReadModel assemble(Order order, Long memberId, StoreOrderViewFeignDTO storeView) {

        Map<Long, String> menuNameMap = storeView.getMenuOrderViewFeignDTOList().stream()
                .collect(Collectors.toMap
                        (MenuOrderViewFeignDTO::getMenuId, MenuOrderViewFeignDTO::getName));

        List<OrderReadModel.OrderMenuDocument> menuList =
                order.getOrderMenuList().stream()
                        .map(om -> toMenuDocument(om, menuNameMap))
                        .toList();

        return OrderReadModel.from(
                order,
                memberId,
                storeView.getCategoryName(),
                storeView.getBrandName(),
                storeView.getStoreName(),
                menuList
        );
    }

    private OrderReadModel.OrderMenuDocument toMenuDocument(
            OrderMenu orderMenu,
            Map<Long, String> menuNameMap) {

        return OrderReadModel.OrderMenuDocument.mapToDoc(
                orderMenu.getId(),
                orderMenu.getMenuId(),
                menuNameMap.get(orderMenu.getMenuId()),
                orderMenu.getPrice(),
                orderMenu.getQuantity(),
                orderMenu.getTotalPrice());
    }
}