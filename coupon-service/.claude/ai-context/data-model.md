# coupon-service 데이터 모델

## MySQL 테이블

### promotion
```sql
CREATE TABLE promotion (
    promotion_id      BIGINT PRIMARY KEY,          -- GenerationType.AUTO
    name              VARCHAR(255),
    start_date_time   DATETIME(6),                 -- 행사 전체 시작 일시
    end_date_time     DATETIME(6),                 -- 행사 전체 종료 일시
    daily_start_time  TIME(6),                     -- 매일 발급 시작 시간
    daily_end_time    TIME(6),                     -- 매일 발급 종료 시간
    promotion_status  VARCHAR(20) NOT NULL,        -- BEFORE | START | END
    created_at        DATETIME(6),
    updated_at        DATETIME(6)
);
```

### coupon
```sql
CREATE TABLE coupon (
    coupon_id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    promotion_id           BIGINT NOT NULL,         -- FK → promotion
    discount_type          VARCHAR(30) NOT NULL,    -- PERCENTAGE | FIXED_AMOUNT | FREE_SHIPPING | FIRST_PURCHASE
    discount_value         INT NOT NULL,
    max_quantity           INT NOT NULL,            -- 최대 발행 수량 (Redis 재고 초기값)
    start_date             DATE,                    -- 쿠폰 발행 시작일
    end_date               DATE,                    -- 쿠폰 발행 종료일
    usage_start_date_time  DATETIME(6),             -- 사용 가능 시작 일시
    usage_end_date_time    DATETIME(6),             -- 사용 가능 종료 일시
    couponstatus           VARCHAR(20) NOT NULL,    -- PENDING | ISSUED  (주의: 오탈자 컬럼명)
    created_at             DATETIME(6),
    updated_at             DATETIME(6)
);
```

### coupon_issue
```sql
CREATE TABLE coupon_issue (
    coupon_issue_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id           BIGINT NOT NULL,
    coupon_id           BIGINT NOT NULL,            -- FK → coupon
    issued_at           DATETIME(6),
    coupon_issue_status VARCHAR(20) NOT NULL,       -- ISSUED | USED
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    UNIQUE KEY uk_coupon_member (member_id, coupon_id)
);
```

### coupon_issue_log
```sql
CREATE TABLE coupon_issue_log (
    coupon_issue_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id           BIGINT,
    coupon_id           BIGINT,
    event_type          VARCHAR(50),               -- SUCCESS | DUPLICATE | OUT_OF_STOCK | INVALID_TIME
    created_at          DATETIME(6),
    updated_at          DATETIME(6)
);
```

## Redis 키 구조

| 키 패턴 | 자료구조 | 용도 |
|---|---|---|
| `coupon:{couponId}` | Hash | CouponCache (stock, startDate, endDate) |
| `coupon:{couponId}:issued` | Set | 발급된 memberId 집합 — 중복 발급 방지 |
| `coupon:{couponId}:ready_to_publish` | Hash | 미발행 이벤트 (key=memberId, value=issuedAt) — 재시도용 |

> `coupon:{couponId}:ready_to_publish` 는 Kafka 발행 성공 시 해당 memberId 항목이 제거된다.  
> 제거되지 않은 항목은 `CouponIssueRetryScheduler`(5초마다)가 재발행한다.
