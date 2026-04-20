# 서비스 간 통신 관계

## 동기 호출 (Feign)

```
[order-service] 주문 생성 시:
  → MEMBERSERVER  GET /internal/members/{id}       (회원 확인)
  → STORESERVER   GET /internal/stores/{id}        (가게 확인)
  → STORESERVER   GET /internal/menus?menuIds=...  (메뉴 조회)
  → COUPONSERVER  GET /internal/coupon-issues/{id} (쿠폰 발급 내역)
  → PAYMENTSERVER POST /internal/payments/create   (결제)

[coupon-service] → MEMBERSERVER GET /internal/members/{id} (쿠폰 발급 시)
[store-service]  → MEMBERSERVER GET /internal/members/{id} (가게 생성 시)
```

## 비동기 통신 (Kafka)

| 토픽 | 발행 | 소비 | 역할 |
|---|---|---|---|
| `coupon_issue` | coupon-service | coupon-service | 선착순 쿠폰 발급 큐 |
| `coupon_log` | coupon-service | coupon-service | 발급 로그 벌크 처리 |
| `orders_created` | order-service | coupon-service | 주문 Saga 시작 |
| `orders_created_result` | coupon-service | order-service | 쿠폰 사용 결과 |

전체 이벤트 스펙 → `.claude/ai-context/kafka-spec.json`

## 핵심 패턴

### Choreography Saga (주문 → 쿠폰 확정)
1. order-service: 주문 생성 → `orders_created` 발행
2. coupon-service: 쿠폰 USED 처리 → `orders_created_result` 발행 (쿠폰 없으면 즉시 성공)
3. order-service: 결과 수신 → 성공=`COMPLETED` / 실패=`CANCELLED` (보상 트랜잭션)

### Outbox 패턴 (order-service)
- `OrderOutboxSaver` (`BEFORE_COMMIT`) → 동일 트랜잭션에서 OutboxEvent DB 저장
- `OrderOutboxPublisher` (`AFTER_COMMIT`) → 커밋 후 Kafka 전송
- `OutboxRetryScheduler` → 10초마다 `READY_TO_PUBLISH` / `FAILED` 이벤트 재시도

### CQRS (order-service)
- 쓰기: MySQL `orders` | 읽기: MongoDB `OrderReadModel`

### Redis Lua (coupon-service)
재고 차감 + 중복 발급 방지를 단일 원자 연산으로 처리.
- 메서드: `CouponRedisAdapter.checkStockAndIssueCoupon`
- 반환값: `0`=재고 부족 / `1`=성공 / `2`=중복
- 키 패턴: `coupon:{couponId}` / `coupon:{couponId}:issued` / `coupon:{couponId}:ready_to_publish`
  > `{}` = Redis Cluster Hash Tag (같은 슬롯 배치)

### 쿠폰 발급 재시도 (coupon-service)
`CouponIssueRetryScheduler` → 5초마다 `ready_to_publish` Hash에서 미발행 이벤트 재발행
