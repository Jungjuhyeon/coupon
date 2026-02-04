package com.example.orderserver.order.domain.model.document;


import com.example.orderserver.order.domain.model.Order;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_read_models") // MongoDB 컬렉션 이름
@CompoundIndexes({ // 여러 복합 인덱스를 선언할 땐 @CompoundIndexes 사용
        @CompoundIndex(def = "{'memberId': 1, 'orderTime': -1}"),
        @CompoundIndex(name = "unique_order_idx", def = "{'orderId': 1}", unique = true)
})
public class OrderReadModel {

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

//    private DiscountType discountType;

    @Getter
    @Builder
    public static class OrderMenuDocument {
        private Long orderMenuId;
        private Long menuId;
        private String name;
        private Integer price;
        private Integer quantity;
        private Integer totalPrice;

        public static OrderMenuDocument mapToDoc(Long orderMenuId, Long menuId,
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
    public static OrderReadModel from(Order order, Long memberId, String storeCategoryName,
                                      String storeBrandName, String storeName, List<OrderMenuDocument> orderMenus) {
//                                                 DiscountType discountType) {
        return OrderReadModel.builder()
                .orderId(order.getId())
                .memberId(memberId)
                .originalPrice(order.getOriginalPrice())
                .discountAmount(order.getDiscountValue())
                .finalPrice(order.getFinalPrice())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus().name())
                .storeCategoryName(storeCategoryName)
                .storeBrandName(storeBrandName)
                .storeName(storeName)
                .orderMenuList(orderMenus)
//                .discountType(discountType)
                .build();
    }

}
