# traffic-coupon — AI Control

## ❗ 절대 규칙 (위반 금지)
- `domain` → 외부 의존성 절대 금지 (순수 Java + JPA 어노테이션만)
- `application` → `infra` 직접 참조 금지 (outputport 인터페이스 통해서만)
- `framework/web` → `inputport` 직접 참조 금지 (usecase 인터페이스 통해서만)
- 도메인 객체 생성 시 `new` 직접 호출 금지 → factory method(`create()`) 사용
- `cancleOrder` 오탈자 수정 금지 (order-service 보상 메서드명)

---

## 📌 시스템 개요
- 대규모 트래픽 선착순 쿠폰 + 주문 플랫폼
- MSA 기반 (member, store, coupon, order, payment)

---

## 🔗 서비스 관계 (핵심)
- order → member, store, coupon, payment
- coupon → member
- store → member

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- 아키텍처 (헥사고날 + DDD): `.claude/ai-context/architecture.md`
- 서비스 통신 / Kafka / Saga 패턴: `.claude/ai-context/service-relations.md`
- 공통 정책 (응답/예외/인증/DB): `.claude/ai-context/common-policy.md`
- 코딩 컨벤션 (네이밍/경로/Feign): `.claude/ai-context/conventions.md`
- 인프라 / 서비스 목록 / 빌드: `.claude/ai-context/infra.md`
- Kafka 이벤트 스펙: `.claude/ai-context/kafka-spec.json`
- Gateway 라우팅: `.claude/ai-context/api-gateway-spec.json`

---

## ⚙️ 사용 규칙
- 작업 시 필요한 문서를 명시적으로 참조해서 사용
- 서비스 구현은 `{service}/.claude/CLAUDE.md` 기준으로 진행
