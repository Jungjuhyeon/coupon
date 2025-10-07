package com.personal_project.coupon.order.application.inputport;

import com.personal_project.coupon.coupon.application.outputport.CouponOutputPort;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.order.application.outputport.OrderOutputPort;
import com.personal_project.coupon.order.application.usecase.InquiryOrderUseCase;
import com.personal_project.coupon.order.domain.model.Order;
import com.personal_project.coupon.order.domain.model.OrderMenu;
import com.personal_project.coupon.order.framwork.web.response.OrderInfoOutPutDTO;
import com.personal_project.coupon.payment.application.outputport.PaymentOutputPort;
import com.personal_project.coupon.payment.domain.model.Payment;
import com.personal_project.coupon.store.domain.model.Brand;
import com.personal_project.coupon.store.domain.model.Menu;
import com.personal_project.coupon.store.domain.model.Store;
import com.personal_project.coupon.store.domain.model.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class InqueryInputPort implements InquiryOrderUseCase {

    private final OrderOutputPort orderOutputPort;
    private final PaymentOutputPort paymentOutputPort;

    @Override
    public OrderInfoOutPutDTO getOrder(Long memberId, Long orderId){

        Order order = orderOutputPort.findOrderDetail(memberId, orderId)
               .orElseThrow(()-> new BusinessException(CommonErrorCode.ORDER_NOT_FOUND));

//        Member member = order.getMember();
        Store store = order.getStore();
        StoreCategory storeCategory = store.getStoreCategory();
        Brand brand = store.getBrand();

        Payment payment = paymentOutputPort.findByOrderId(orderId)
                .orElseThrow(()-> new BusinessException(CommonErrorCode.PAYMENT_NOT_FOUND));

        return OrderInfoOutPutDTO.mapToDTO(order,storeCategory.getName(),brand.getName(),store.getName(),
                payment.getAmount());
    }

}
