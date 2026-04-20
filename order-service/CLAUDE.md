# order-service

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- 주문 생성, 조회, 통계, Saga 오케스트레이션 담당
- Eureka: `ORDERSERVER` / Port: `8084`
- DB: MySQL (orders, order_menu, outbox_event) + MongoDB (order_read_models)
- 패턴: Outbox + CQRS + Choreography Saga

---

## 🔗 서비스 관계

| 방향 | 서비스 | 방식 | 목적 |
|---|---|---|---|
| 호출 | MEMBERSERVER | Feign | 주문 생성 시 회원 검증 |
| 호출 | STORESERVER | Feign | 가게 확인, 메뉴 가격/뷰 조회 |
| 호출 | COUPONSERVER | Feign | 쿠폰 발급 내역 조회 (optional) |
| 호출 | PAYMENTSERVER | Feign | 결제 생성 |
| 발행 | coupon-service | Kafka `orders_created` (Outbox) | Saga 시작 |
| 소비 | coupon-service | Kafka `orders_created_result` | Saga 결과 수신 |

---

## 📌 패키지 구조
서브도메인 2개:
- `order/` → 주문 도메인 (핵심)
- `outbox/` → Outbox 패턴 (발행 보장)

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- 도메인 / 유스케이스: `.claude/ai-context/domain-overview.md`
- 데이터 모델: `.claude/ai-context/data-model.md`
- API 명세: `.claude/ai-context/api-spec.json`
- Kafka 스펙: `.claude/ai-context/kafka-spec.json`
