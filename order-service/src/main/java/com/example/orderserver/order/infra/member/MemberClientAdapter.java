package com.example.orderserver.order.infra.member;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.outputport.MemberOutputPort;
import com.example.orderserver.order.exception.OrderErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberClientAdapter implements MemberOutputPort {
    private final MemberFeignClient memberFeignClient;
    @Override
    public void validateMember(Long memberId){
        try {
            memberFeignClient.validateMember(memberId);
        } catch (FeignException.NotFound e) {
            throw new BusinessException(OrderErrorCode.ORDER_PRECONDITION_FAILED);
        }    }

}
