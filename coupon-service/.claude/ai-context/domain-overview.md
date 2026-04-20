# coupon-service — Domain + UseCase

---

## Domain

### Aggregate / Entity

**Promotion**
- `Promotion.create(dto)` → promotionStatus = START (하드코딩)

**Coupon**
- `Coupon.create(promotion, dto)` → couponstatus = ISSUED (주의: 오탈자 컬럼명)
- `coupon.isUsableNow(now)` → usageStartDateTime <= now < usageEndDateTime

**CouponIssue** (1인 1쿠폰 unique: member_id + coupon_id)
- `CouponIssue.create(memberId, coupon, now)` → couponIssueStatus = ISSUED
- `CouponIssue.createCouponIssueEvent(couponId, memberId, curTime)` → CouponIssuedEvent
- `couponIssue.isUsable()` → couponIssueStatus == ISSUED

**CouponIssueLog**
- `CouponIssueLog.create(memberId, couponId, eventType)`
- `CouponIssueLog.createCouponIssuedLogEvent(...)` → CouponIssuedLogEvent

### Value Object (enum)

| Enum | 값 |
|---|---|
| `PromotionStatus` | BEFORE / START(기본) / END |
| `CouponStatus` | PENDING / ISSUED(기본) |
| `CouponIssueStatus` | ISSUED / USED |
| `DiscountType` | PERCENTAGE / FIXED_AMOUNT / FREE_SHIPPING / FIRST_PURCHASE |
| `EventType` | SUCCESS / DUPLICATE / OUT_OF_STOCK / INVALID_TIME |

### Redis 캐시 (비JPA, domain/model/cache/)

**CouponCache** (Hash `coupon:{couponId}`)
- `CouponCache.create(id, stock, startDate, endDate)`
- `couponCache.isValid(now)` → 쿠폰 발행 기간 내인지 확인

**PromotionCache**
- `PromotionCache.create(id, dailyStartTime, dailyEndTime, startDateTime, endDateTime)`
- `promotionCache.isValid(now)` → 행사 전체 기간 + 당일 발급 시간 검증

### Domain Event (domain/model/event/)

| 이벤트 클래스 | 토픽 | 방향 | 주요 필드 |
|---|---|---|---|
| `CouponIssuedEvent` | `coupon_issue` | produce | couponId, memberId, currentTime |
| `CouponIssuedLogEvent` | `coupon_log` | produce | couponId, memberId, issueDateTime, eventType |
| `OrderCreatedEvent` | `orders_created` | consume | orderId, memberId, couponIssueId |
| `OrderCreatedEventResult` | `orders_created_result` | produce | orderId, memberId, couponIssueId, successed |

`OrderCreatedEventResult`: `.success()` / `.fail()` 체이닝으로 successed 세팅

---

## UseCase (Incoming Port)

| 인터페이스 | 구현체 | 설명 |
|---|---|---|
| `IssueCouponUseCase` | `IssueCouponInputPort` | 선착순 쿠폰 발급 |
| `AddCouponUseCase` | `AddCouponInputPort` | 쿠폰 정책 생성 |
| `AddPromotionUseCase` | `AddPromotionInputPort` | 프로모션 생성 |
| `AddCouponIssueUseCase` | `AddCouponIssueInputPort` | Kafka 수신 후 CouponIssue MySQL 저장 |
| `AddCouponIssueLogUseCase` | `AddCouponIssueLogInputPort` | 발급 로그 in-memory 버퍼 → 벌크 저장 |
| `CouponIssueMakeUsedUseCase` | `CouponIssueMakeUsedInputPort` | Saga — 쿠폰 USED 처리 |
| `InquiryCouponIssueUseCase` | `InquiryCouponIssueInputPort` | 쿠폰 발급 내역 조회 (order-service Feign용) |
| `InquiryCouponUseCase` | `InquiryCouponInputPort` | 쿠폰 정책 조회 |

### 주요 흐름

**IssueCouponInputPort.issue(promotionId, couponId, memberId)**
1. `PromotionValidator.validate()` → PromotionCache로 행사 기간 + 당일 시간 검증
2. `CouponValidator.validate()` → CouponCache로 쿠폰 발행 기간 검증, endDate 반환
3. `CouponIssuer.issue()` → Redis Lua (0=재고부족, 2=중복, 1=성공)
4. `couponIssueEventPublisher.publishEvent()` → `coupon_issue` 토픽 발행
5. `couponIssueEventPublisher.publishSuccessLog()` → `coupon_log` SUCCESS 로그 발행

**CouponIssuedConsumer.consumeIssue()** (`coupon_issue` 소비)
1. `memberOutputPort.existsById(memberId)` → Feign MEMBERSERVER 회원 존재 확인
2. `inquiryCouponUseCase.getCouponById(couponId)` → Coupon 조회
3. `addCouponIssueUseCase.addCouponIssue()` → CouponIssue MySQL 저장 (DataIntegrityViolation 시 중복 무시)

**AddCouponIssueLogInputPort** (in-memory 버퍼 패턴)
- `addCouponIssue()` → `ConcurrentLinkedQueue` 버퍼에 적재
- `flush()` → 버퍼 drain 후 `couponIssueLogOutputPort.saveAll()` 벌크 저장
- `CouponIssuedConsumer.flushLogs()` (`@Scheduled fixedRate=10000`)가 flush() 호출

**AddCouponInputPort.addCoupon(dto)**
1. `promotionOutputPort.findById()` → Promotion 조회
2. `Coupon.create()` → `couponOutputPort.save()`
3. `couponCacheOutputPort.saveCouponData()` → Redis 재고 초기화

**CouponIssueMakeUsedInputPort.used(couponIssueId)**
- `couponIssueOutputPort.useCoupon()` → ISSUED → USED (0건이면 COUPON_ALREADY_USED 예외)

**CouponIssueRetryScheduler** (`@Scheduled fixedDelay=5000`)
- `coupon:{id}:ready_to_publish` Hash에서 미발행 이벤트 재발행

