package com.example.orderserver.order.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.orderserver.order.application.outputport.OrderOutputPort;
import com.example.orderserver.order.application.outputport.OrderReadModelOutputPort;
import com.example.orderserver.order.application.usecase.CompensationUsecase;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.document.OrderReadModel;
import com.example.orderserver.order.exception.OrderErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompensationInputPort implements CompensationUsecase {
    private final OrderOutputPort orderOutputPort;
    private final OrderReadModelOutputPort orderReadModelOutputPort;
    private final String cancelStatus = "CANCELLED";
    private final String completeStatus = "COMPLETED";

    @Override
    public void cancleOrder(Long orderId, Long memberId){
        Order order = orderOutputPort.findById(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        order.cancel();

        orderReadModelOutputPort.updateStatus(orderId,cancelStatus);
    }

    @Override
    public void successOrder(Long orderId, Long memberId){
        Order order = orderOutputPort.findById(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        order.complete();

        orderReadModelOutputPort.updateStatus(orderId,completeStatus);
    }

}
