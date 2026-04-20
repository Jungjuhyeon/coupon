# order-service — Domain + UseCase

서브도메인 2개: `order`(핵심 주문) + `outbox`(발행 보장)

---

## Domain

### Aggregate Root: Order

**팩토리 / 도메인 메서드**
- `Order.create(memberId, storeId, deliveryAddress, comment)` → orderStatus = PENDING, orderTime = now
- `Order.createOrderEvent(memberId, orderId, couponIssueId, eventType)` → OrderCreatedEvent (Outbox 발행용)
- `order.finalizePriceWithCoupon(couponIssueId, discountValue)` → originalPrice=메뉴합계, finalPrice=원가-할인
- `order.finalizePriceWithoutCoupon()` → couponIssueId=null, discountValue=0, finalPrice=originalPrice
- `order.addOrderMenu(orderMenu)` / `order.addOrderMenus(list)`
- `order.cancel()` → orderStatus=CANCELLED + 모든 OrderMenu.cancel()
- `order.complete()` → orderStatus=COMPLETED + 모든 OrderMenu.complete()

**OrderStatus enum**: PENDING(기본) / ACCEPTED / IN_PROGRESS / READY / COMPLETED / CANCELLED / FAILED / REFUNDED

### Child Entity: OrderMenu

- `OrderMenu.create(order, menuId, menuPrice, quantity)` → totalPrice=menuPrice*quantity, orderStatus=PENDING
- `orderMenu.changeOrder(order)` — Order.addOrderMenu()에서만 호출, 직접 호출 금지
- `orderMenu.cancel()` / `orderMenu.complete()`

### Read Model: OrderReadModel (MongoDB)

- `OrderReadModel.from(order, memberId, storeCategoryName, storeBrandName, storeName, orderMenus)`
- `orderReadModel.cancel()` → orderStatus="CANCELLED"
- `orderReadModel.complete()` → orderStatus="COMPLETED"
- 내부: `OrderReadModel.OrderMenuDocument.mapToDoc(...)`

### Domain Event

| 이벤트 클래스 | 토픽 | 방향 | 주요 필드 |
|---|---|---|---|
| `OrderCreatedEvent` | `orders_created` | produce (Outbox) | orderId, memberId, couponIssueId, eventType |
| `OrderCreatedEventResult` | `orders_created_result` | consume | orderId, memberId, couponIssueId, successed |

`OrderCreatedEventResult`: `result.isSuccess()` → successed 반환

---

### Outbox 서브도메인: OutboxEvent

- `OutboxEvent.create(aggregateType, aggregateId, eventType, payload)` → status=READY_TO_PUBLISH, createdAt=now
- `outboxEvent.markOutboxEventPending()` → PUBLISHED
- `outboxEvent.markOutboxEventFailed()` → FAILED
- `outboxEvent.markOutboxEventProcessed()` → MESSAGE_CONSUME

**OutboxEventStatus enum**: READY_TO_PUBLISH(기본) / PUBLISHED / MESSAGE_CONSUME / FAILED

---

## UseCase (Incoming Port)

| 인터페이스 | 구현체 | 설명 |
|---|---|---|
| `AddOrderUseCase` | `AddOrderInputPort` | 주문 생성 오케스트레이션 |
| `CompensationUsecase` | `CompensationInputPort` | Saga 보상 (cancleOrder, successOrder) |
| `AddOrderReadModelUseCase` | `AddOrderReadModelInputPort` | MongoDB 읽기 모델 저장 |
| `InquiryOrderUseCase` | `InquiryInputPort` | 주문 상세/목록 조회 |
| `InquiryStatisticsUseCase` | `InquiryStatisticsInputPort` | 월별 주문 통계 |
| `OutboxUseCase` | `OutboxInputPort` | Outbox 이벤트 관리 |

### 주요 흐름

**AddOrderInputPort.create(memberId, storeId, OrderInputDTO)**
1. `memberOutputPort.validateMember(memberId)` → Feign MEMBERSERVER 회원 확인
2. `storeOutputPort.validateStore(storeId)` → Feign STORESERVER 가게 확인
3. `couponApplier.loadCouponIfExists(couponIssueId)` → couponIssueId=null이면 skip, 있으면 Feign COUPONSERVER
4. `Order.create()` → PENDING
5. `orderFactory.createOrderMenus()` → Feign STORESERVER 메뉴 가격 조회 → `OrderMenu.create()`
6. `order.addOrderMenus()`
7. `couponApplier.applyPricing()` → finalizePriceWithCoupon or WithoutCoupon
8. `orderOutputPort.save(order)` → MySQL
9. `paymentOutputPort.save(orderId, finalPrice)` → Feign PAYMENTSERVER
10. `orderEventPublisher.publishOrderCreated()` → OutboxEvent 생성
    - `OrderOutboxSaver` (BEFORE_COMMIT) → OutboxEvent DB 저장
    - `OrderOutboxPublisher` (AFTER_COMMIT) → Kafka `orders_created` 발행

**CompensationInputPort** (orders_created_result 소비 후 호출)
- `cancleOrder(orderId, memberId)` → `order.cancel()` + `orderReadModelOutputPort.updateStatus("CANCELLED")`
  > **주의**: 메서드명 `cancleOrder` 오탈자 — 수정 금지 (루트 CLAUDE.md 절대 규칙)
- `successOrder(orderId, memberId)` → `order.complete()` + `orderReadModelOutputPort.updateStatus("COMPLETED")`

**OrderCreatedConsumer** (`orders_created` 소비 → MongoDB ReadModel 생성)
1. OrderCreatedEvent 역직렬화
2. `inquiryOrderUseCase.getOrderById()` → MySQL Order 조회
3. `storeOutputPort.getRequiredStoreOrderView()` → Feign STORESERVER
4. `OrderReadModel.from()` → `addOrderReadModelUseCase.addOrderReadModel()` → MongoDB upsert

**OutboxRetryScheduler** (`@Scheduled fixedDelay=10000`)
- READY_TO_PUBLISH / FAILED 상태 OutboxEvent 재발행
