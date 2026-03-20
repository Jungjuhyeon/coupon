package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.document.OrderReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RequiredArgsConstructor
public class OrderReadModelCustomRepositoryImpl implements OrderReadModelCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void upsertOrderCreated(OrderReadModel doc) {
        Query query = new Query(Criteria.where("orderId").is(doc.getOrderId()));
        Update update = new Update()
                .set("memberId", doc.getMemberId())
                .set("originalPrice", doc.getOriginalPrice())
                .set("discountAmount", doc.getDiscountAmount())
                .set("finalPrice", doc.getFinalPrice())
                .set("orderTime", doc.getOrderTime())
                .set("storeCategoryName", doc.getStoreCategoryName())
                .set("storeBrandName", doc.getStoreBrandName())
                .set("storeName", doc.getStoreName())
                .set("orderMenuList", doc.getOrderMenuList())
                .set("_class", OrderReadModel.class.getName())
                // 핵심: 문서가 처음 생성될 때만 상태를 기록. 이미 CANCELLED가 있으면 유지됨.
                .setOnInsert("orderStatus", doc.getOrderStatus());

        mongoTemplate.upsert(query, update, OrderReadModel.class);
    }

    @Override
    public void updateStatus(Long orderId, String status) {
        Query query = new Query(Criteria.where("orderId").is(orderId));
        Update update = new Update().set("orderStatus", status);

        // 데이터가 아직 없어도 'CANCELLED' 상태부터 미리 생성(Upsert)함
        mongoTemplate.upsert(query, update, OrderReadModel.class);
    }
}