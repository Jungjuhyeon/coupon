package com.personal_project.coupon.order.framwork.web.response;

import com.personal_project.coupon.coupon.domain.model.enumeration.DiscountType;
import com.personal_project.coupon.order.domain.model.Order;
import com.personal_project.coupon.order.domain.model.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class OrderSummaryOutputDTO {

    private Long orderId;
    private Integer originalPrice;
    private Integer discountAmount;
    private Integer finalPrice;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;

    private String storeCategoryName;
    private String storeBrandName;
    private String storeName;

    private List<OrderMenuOutputDTO> orderMenuOutputDTOList;

    private DiscountType discountType;

    public static OrderSummaryOutputDTO mapToDTO(Order order,String storeCategoryName,
                                                 String storeBrandName, String storeName,
                                                 DiscountType discountType){
        return OrderSummaryOutputDTO.builder()
                .orderId(order.getId())
                .originalPrice(order.getOriginalPrice())
                .discountAmount(order.getDiscountAmount())
                .finalPrice(order.getFinalPrice())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus())
                .storeCategoryName(storeCategoryName)
                .storeBrandName(storeBrandName)
                .storeName(storeName)
                .orderMenuOutputDTOList(order.getOrderMenuList().stream()
                .map(o-> OrderMenuOutputDTO.mapToDTO(o.getId(),o.getMenu().getId(),
                        o.getMenu().getName(),o.getPrice(),o.getQuantity(),o.getTotalPrice())).collect(Collectors.toList()))
                .discountType(discountType)
                .build();
    }
}
