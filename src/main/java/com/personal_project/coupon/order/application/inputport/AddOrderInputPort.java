package com.personal_project.coupon.order.application.inputport;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutputPort;
import com.personal_project.coupon.coupon.domain.model.CouponIssue;
import com.personal_project.coupon.coupon.domain.model.enumeration.CouponIssueStatus;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.order.application.outputport.OrderEventOutputPort;
import com.personal_project.coupon.order.application.outputport.OrderOutputPort;
import com.personal_project.coupon.order.application.usecase.AddOrderUseCase;
import com.personal_project.coupon.order.domain.model.Order;
import com.personal_project.coupon.order.domain.model.OrderMenu;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import com.personal_project.coupon.order.framwork.web.request.OrderInputDTO;
import com.personal_project.coupon.order.framwork.web.request.OrderMenuInfoDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderOutputDTO;
import com.personal_project.coupon.payment.application.outputport.PaymentOutputPort;
import com.personal_project.coupon.payment.domain.model.Payment;
import com.personal_project.coupon.store.application.outputport.MenuOutputPort;
import com.personal_project.coupon.store.application.outputport.StoreOutputPort;
import com.personal_project.coupon.store.domain.model.Menu;
import com.personal_project.coupon.store.domain.model.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.personal_project.coupon.order.domain.model.Order.createOrderEvent;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddOrderInputPort implements AddOrderUseCase {

    private final MemberOutputPort memberOutputPort;
    private final StoreOutputPort storeOutputPort;
    private final CouponIssueOutputPort couponIssueOutputPort;
    private final MenuOutputPort menuOutputPort;
    private final OrderOutputPort orderOutputPort;
    private final PaymentOutputPort paymentOutputPort;
    private final OrderEventOutputPort orderEventOutputPort;

    @Override
    @Transactional
    public OrderOutputDTO create(Long memberId, Long storeId, OrderInputDTO request) throws JsonProcessingException {
        Member member = memberOutputPort.findById(memberId)
                .orElseThrow(()-> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

        Store store = storeOutputPort.findById(storeId)
                .orElseThrow(()-> new BusinessException(CommonErrorCode.STORE_NOT_FOUND));

        CouponIssue couponIssue = Optional.ofNullable(request.getCouponIssueId())
                .map(id -> couponIssueOutputPort.findByIdAndMemberId(id,memberId)
                        .orElseThrow(() -> new BusinessException(CommonErrorCode.COUPON_NOT_FOUND)))
                .orElse(null);

        //쿠폰 검증로직
        validateAndUseCoupon(couponIssue);

        Order order = Order.create(member, store, couponIssue, request.getDeliveryAddress(),request.getComment());

        // menuId 리스트 추출
        List<Long> menuIds = request.getOrderMenuInfoDTOList().stream()
                .map(OrderMenuInfoDTO::getMenuId)
                .toList();

        Map<Long, Menu> menuMap = menuOutputPort.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, m -> m));

        List<OrderMenu> orderMenus = request.getOrderMenuInfoDTOList().stream()
                .map(dto -> OrderMenu.create(order, menuMap.get(dto.getMenuId()), dto.getCount()))
                .toList();

        order.addOrderMenus(orderMenus);
        //할인율 등 수행
        order.applyCoupon(couponIssue);

        orderOutputPort.save(order);

        paymentOutputPort.save(Payment.create(order,order.getFinalPrice()));

        //이벤트 발행
        OrderCreatedEvent orderCreatedEvent = createOrderEvent(memberId,order.getId());
        orderEventOutputPort.occurOrderEvent(orderCreatedEvent);

        return OrderOutputDTO.mapToDTO(order.getId());
    }

    private void validateAndUseCoupon(CouponIssue couponIssue) {
        if (couponIssue == null) {
            return; // 쿠폰 없는 경우 그냥 통과
        }
        //사용여부
        if (couponIssue.getCouponIssueStatus() == CouponIssueStatus.USED) {
            throw new BusinessException(CommonErrorCode.COUPON_ALREADY_USED);
        }
        //사용기간 여부
        if (!couponIssue.getCoupon().isUsableNow(LocalDateTime.now())) {
            throw new BusinessException(CommonErrorCode.COUPON_EXPIRED);
        }

        // 최종적으로 사용 처리 (상태 변경만 수행)
        couponIssue.couponUse();
    }
}
