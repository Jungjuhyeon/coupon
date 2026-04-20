# coupon-service

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- 선착순 쿠폰 발급, 프로모션 관리, 쿠폰 사용 처리 (Saga 참여)
- Eureka: `COUPONSERVER` / Port: `8083`
- DB: MySQL + Redis Cluster (재고·발급 관리)

---

## 🔗 서비스 관계

| 방향 | 서비스 | 방식 | 목적 |
|---|---|---|---|
| 호출 | MEMBERSERVER | Feign | 쿠폰 발급 시 회원 검증 |
| 피호출 | order-service | Feign | 주문 생성 시 쿠폰 발급 내역 조회 |
| 소비 | order-service | Kafka `orders_created` | 주문 이벤트 → 쿠폰 USED 처리 |
| 발행 | order-service | Kafka `orders_created_result` | 쿠폰 사용 결과 통지 |

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- 도메인 / 유스케이스: `.claude/ai-context/domain-overview.md`
- 데이터 모델: `.claude/ai-context/data-model.md`
- API 명세: `.claude/ai-context/api-spec.json`
- Kafka 스펙: `.claude/ai-context/kafka-spec.json`
