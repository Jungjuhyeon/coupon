# payment-service — Domain + UseCase

---

## Domain

### Aggregate: Payment

- `Payment.create(orderId, amount)` → 생성자 private, 반드시 팩토리 메서드 사용

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long (PK, IDENTITY) | 결제 식별자 |
| orderId | Long | 연관 주문 ID (order-service 참조) |
| amount | Integer | 결제 금액 |

> `PaymentStatus`, `PaymentMethod` enum 선언되어 있으나 값 없음 (미구현)

---

## UseCase (Incoming Port)

| 인터페이스 | 구현체 | 설명 |
|---|---|---|
| `addPaymentUsecase` | `addPaymentInputPort` | 결제 내역 저장 (클래스명 소문자 시작 — 네이밍 위반, 수정 금지) |

### 주요 흐름

**addPaymentInputPort.create(orderId, amount)**
1. `Payment.create(orderId, amount)`
2. `paymentOutputPort.save(payment)` → MySQL 저장
