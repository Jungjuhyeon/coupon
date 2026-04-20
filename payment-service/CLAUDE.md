# payment-service

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- order-service의 Feign 호출을 받아 결제 내역 저장, 외부 공개 API 없음
- Eureka: `PAYMENTSERVER` / Port: `8085`
- DB: MySQL (payment)

---

## 🔗 서비스 관계

| 방향 | 서비스 | 방식 | 목적 |
|---|---|---|---|
| 피호출 | order-service | Feign | 결제 내역 생성 |

Feign 호출 없음. Kafka 미사용.

---

## 📌 패키지 구조 특이사항
- 컨트롤러 패키지: `controller/` (타 서비스의 `framework/web/`과 다름)
- UseCase/InputPort 클래스명 소문자 시작: `addPaymentUsecase`, `addPaymentInputPort` — 네이밍 컨벤션 위반, 수정 금지

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- 도메인 / 유스케이스: `.claude/ai-context/domain-overview.md`
- 데이터 모델: `.claude/ai-context/data-model.md`
- API 명세: `.claude/ai-context/api-spec.json`
