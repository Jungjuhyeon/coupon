package com.example.orderserver.order.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.assembler.OrderDetailAssembler;
import com.example.orderserver.order.application.outputport.OrderOutputPort;
import com.example.orderserver.order.application.outputport.OrderSummaryOutputPort;
import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.application.usecase.InquiryOrderUseCase;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
import com.example.orderserver.order.exception.OrderErrorCode;
import com.example.orderserver.order.framwork.web.response.OrderInfoOutPutDTO;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InqueryInputPort implements InquiryOrderUseCase {

    private final OrderOutputPort orderOutputPort;
    private final StoreOutputPort storeOutputPort;
    private final OrderDetailAssembler orderDetailAssembler;
    private final OrderSummaryOutputPort orderSummaryOutputPort;

    @Override
    public OrderInfoOutPutDTO getOrderDetail(Long memberId, Long orderId){

        Order order = orderOutputPort.findById(orderId)
               .orElseThrow(()-> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        StoreOrderViewFeignDTO storeOrderView =
                storeOutputPort.getStoreOrderView(order.getStoreId(), order.getOrderMenuList().stream()
                                .map(OrderMenu::getMenuId).toList());

        //        Payment payment = paymentOutputPort.findByOrderId(orderId)
//                .orElseThrow(()-> new BusinessException(CommonErrorCode.PAYMENT_NOT_FOUND));

        return orderDetailAssembler.assemble(order, storeOrderView);

    }

    @Override
    public List<OrderSummaryDocument> getOrder(Long memberId){
        return orderSummaryOutputPort.findByMemberId(memberId);
    }

}
