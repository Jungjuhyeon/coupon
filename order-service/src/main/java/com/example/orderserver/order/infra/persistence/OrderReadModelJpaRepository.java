package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.document.OrderReadModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderReadModelJpaRepository extends MongoRepository<OrderReadModel,Long>,OrderReadModelCustomRepository {

    List<OrderReadModel> findTop20ByMemberIdOrderByOrderTimeDesc(Long memberId);

    Optional<OrderReadModel> findByOrderId(Long orderId);
}
