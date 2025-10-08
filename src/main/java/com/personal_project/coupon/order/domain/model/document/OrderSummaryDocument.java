package com.personal_project.coupon.order.domain.model.document;

import com.personal_project.coupon.coupon.domain.model.enumeration.DiscountType;
import com.personal_project.coupon.order.domain.model.Order;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders_summaries") // MongoDB 컬렉션 이름
@CompoundIndex(def = "{'memberId': 1, 'orderTime': -1}")
public class OrderSummaryDocument {

    @Id
    private String id;

    private Long memberId;
    private Long orderId;
    private Integer originalPrice;
    private Integer discountAmount;
    private Integer finalPrice;
    private LocalDateTime orderTime;
    private String orderStatus;

    private String storeCategoryName;
    private String storeBrandName;
    private String storeName;

    private List<OrderMenuDocument> orderMenuList;

    private DiscountType discountType;

    @Getter
    @Builder
    public static class OrderMenuDocument {
        private Long orderMenuId;
        private Long menuId;
        private String name;
        private Integer price;
        private Integer quantity;
        private Integer totalPrice;

        public static OrderMenuDocument mapToDTO(Long orderMenuId, Long menuId,
                                                 String name, Integer price,
                                                 Integer quantity, Integer totalPrice){
            return OrderMenuDocument.builder()
                    .orderMenuId(orderMenuId)
                    .menuId(menuId)
                    .name(name)
                    .price(price)
                    .quantity(quantity)
                    .totalPrice(totalPrice)
                    .build();
        }
    }

    // DTO → Document 변환 메서드
    public static OrderSummaryDocument fromEvent(Order order,Long memberId, String storeCategoryName,
                                               String storeBrandName, String storeName,
                                               DiscountType discountType) {
        return OrderSummaryDocument.builder()
                .orderId(order.getId())
                .memberId(memberId)
                .originalPrice(order.getOriginalPrice())
                .discountAmount(order.getDiscountAmount())
                .finalPrice(order.getFinalPrice())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus().name())
                .storeCategoryName(storeCategoryName)
                .storeBrandName(storeBrandName)
                .storeName(storeName)
                .orderMenuList(order.getOrderMenuList().stream()
                        .map(o-> OrderMenuDocument.mapToDTO(o.getId(),o.getMenu().getId(),
                                o.getMenu().getName(),o.getPrice(),o.getQuantity(),o.getTotalPrice())).collect(Collectors.toList()))
                .discountType(discountType)
                .build();
    }

}
