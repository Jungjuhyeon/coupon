package com.example.orderserver.order.framwork.web.response;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderInfoOutPutDTO {
    private Long orderId;
    private Integer originalPrice;
    private Integer discountValue;
    private Integer finalPrice;
    private String deliveryAddress;
    private LocalDateTime orderTime;
    private String comment;
    private OrderStatus orderStatus;

    private String storeCategoryName;
    private String storeBrandName;
    private String storeName;

    private List<OrderMenuOutputDTO> orderMenuOutputDTOList;

//    private Integer amount;

    public static OrderInfoOutPutDTO mapToDTO(Order order, String storeCategoryName, String storeBrandName,
                                              String storeName, List<OrderMenuOutputDTO> orderMenus ){ // Integer amount){
        return OrderInfoOutPutDTO.builder()
                .orderId(order.getId())
                .originalPrice(order.getOriginalPrice())
                .discountValue(order.getDiscountValue())
                .finalPrice(order.getFinalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .orderTime(order.getOrderTime())
                .comment(order.getComment())
                .orderStatus(order.getOrderStatus())
                .storeCategoryName(storeCategoryName)
                .storeBrandName(storeBrandName)
                .storeName(storeName)
                .orderMenuOutputDTOList(orderMenus)
                .build();
    }
}
