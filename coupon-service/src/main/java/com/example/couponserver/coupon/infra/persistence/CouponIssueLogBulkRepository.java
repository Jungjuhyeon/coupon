package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.application.outputport.CouponIssueLogOutputPort;
import com.example.couponserver.coupon.domain.model.CouponIssueLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssueLogBulkRepository implements CouponIssueLogOutputPort {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<CouponIssueLog> batch){
        LocalDateTime now = LocalDateTime.now();
        String sql = "INSERT INTO coupon_issue_log (coupon_id,member_id,event_type,created_at,updated_at)"+
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                batch,
                batch.size(),
                (PreparedStatement ps, CouponIssueLog couponIssueLog) -> {
                    ps.setLong(1, couponIssueLog.getCouponId());
                    ps.setLong(2, couponIssueLog.getMemberId());
                    ps.setString(3, couponIssueLog.getEventType().name());
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                    ps.setTimestamp(5, Timestamp.valueOf(now));
                });
    }
}


