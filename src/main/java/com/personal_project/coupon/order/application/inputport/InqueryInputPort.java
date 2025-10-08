package com.personal_project.coupon.order.application.inputport;

import com.personal_project.coupon.coupon.domain.model.enumeration.DiscountType;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.order.application.outputport.OrderOutputPort;
import com.personal_project.coupon.order.application.usecase.InquiryOrderUseCase;
import com.personal_project.coupon.order.domain.model.Order;
import com.personal_project.coupon.order.framwork.web.response.OrderInfoOutPutDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderSummaryOutputDTO;
import com.personal_project.coupon.payment.application.outputport.PaymentOutputPort;
import com.personal_project.coupon.payment.domain.model.Payment;
import com.personal_project.coupon.store.domain.model.Brand;
import com.personal_project.coupon.store.domain.model.Store;
import com.personal_project.coupon.store.domain.model.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class InqueryInputPort implements InquiryOrderUseCase {

    private final OrderOutputPort orderOutputPort;
    private final PaymentOutputPort paymentOutputPort;

    @Override
    public OrderInfoOutPutDTO getOrderDetail(Long memberId, Long orderId){

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

    @Override
    public List<OrderSummaryOutputDTO> getOrder(Long memberId){
        List<Order> orderList = orderOutputPort.findOrderDetail(memberId);


        return orderList.stream()
                        .map(o-> {
                            Store store = o.getStore();
                            StoreCategory storeCategory = store.getStoreCategory();
                            Brand brand = store.getBrand();
                            DiscountType discountType = Optional.ofNullable(o.getCouponIssue())
                                    .map(ci -> ci.getCoupon().getDiscountType())
                                    .orElse(null); // 또는 기본값 지정

                            return OrderSummaryOutputDTO.mapToDTO(o,storeCategory.getName(),brand.getName(),
                                    store.getName(), discountType);
                                }
                        ).toList();
    }


}
