package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.document.OrderReadModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderReadModelJpaRepository extends MongoRepository<OrderReadModel,Long> {

    List<OrderReadModel> findTop20ByMemberIdOrderByOrderTimeDesc(Long memberId);

}
