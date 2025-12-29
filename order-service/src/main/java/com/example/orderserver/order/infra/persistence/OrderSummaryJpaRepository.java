package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderSummaryJpaRepository extends MongoRepository<OrderSummaryDocument,Long> {

    List<OrderSummaryDocument> findTop20ByMemberIdOrderByOrderTimeDesc(Long memberId);

}
