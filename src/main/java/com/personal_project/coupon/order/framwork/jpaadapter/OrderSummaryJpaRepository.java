package com.personal_project.coupon.order.framwork.jpaadapter;

import com.personal_project.coupon.order.domain.model.document.OrderSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderSummaryJpaRepository extends MongoRepository<OrderSummaryDocument,Long> {

    List<OrderSummaryDocument> findTop20ByMemberIdOrderByOrderTimeDesc(Long memberId);

}
