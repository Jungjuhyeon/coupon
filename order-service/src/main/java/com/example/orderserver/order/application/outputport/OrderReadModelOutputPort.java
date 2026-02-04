package com.example.orderserver.order.application.outputport;


import com.example.orderserver.order.domain.model.document.OrderReadModel;

import java.util.List;

public interface OrderReadModelOutputPort {

    OrderReadModel save(OrderReadModel orderReadModel);

    List<OrderReadModel> findByMemberId(Long memberId);

}

