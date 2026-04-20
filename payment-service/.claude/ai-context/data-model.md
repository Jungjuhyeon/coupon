# payment-service 데이터 모델

## MySQL 테이블

### payment
```sql
CREATE TABLE payment (
    payment_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,                     -- order-service 주문 ID
    amount      INT,                                 -- 결제 금액
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);
```

> `PaymentStatus`, `PaymentMethod` enum 선언되어 있으나 값 없음 (미구현).
> 현재 payment 테이블에 상태/수단 컬럼 없음.
