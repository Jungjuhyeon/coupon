package com.personal_project.coupon.coupon.framwork.kafkaadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutputPort;
import com.personal_project.coupon.coupon.application.outputport.CouponOutputPort;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import com.personal_project.coupon.coupon.domain.model.CouponIssue;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedEvent;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponIssuedConsumer {
    private final ObjectMapper objectMapper;
    private final MemberOutputPort memberOutputPort;
    private final CouponOutputPort couponOutputPort;
    private final CouponIssueOutputPort couponIssueOutputPort;

    @KafkaListener(topics = "${consumer.topic1.name}", groupId = "${consumer.groupid.name}")
    public void consumeIssue(ConsumerRecord<String,String> record) throws IOException{
        System.out.println("issue:" + record.value());

        CouponIssuedEvent couponIssuedEvent = objectMapper.readValue(record.value(),CouponIssuedEvent.class);

        Member member = memberOutputPort.findById(couponIssuedEvent.getMemberId())
                .orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponOutputPort.findById(couponIssuedEvent.getCouponId())
                .orElseThrow(() -> new BusinessException(CommonErrorCode.COUPON_NOT_FOUND));

//        coupon.increaseStock();

        CouponIssue couponIssue = CouponIssue.create(member, coupon, couponIssuedEvent.getCurrentTime());
        couponIssueOutputPort.save(couponIssue);
    }

}
