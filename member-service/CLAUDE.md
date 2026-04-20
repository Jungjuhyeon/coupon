# member-service

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- 회원 가입, 로그인, JWT 발급 및 내부 회원 검증 담당
- Eureka: `MEMBERSERVER` / Port: `8081`
- DB: MySQL (`members` 테이블) — Redis/MongoDB 없음

---

## 🔗 서비스 관계
- Feign 호출 없음 (다른 서비스를 호출하지 않음)
- 피호출 서비스:

| 호출자 | 경로 | 목적 |
|---|---|---|
| order / coupon / store | `GET /internal/members/{memberId}` | 회원 존재 확인 |
| order | `GET /internal/members/profile/{memberId}` | 회원 프로필 조회 |

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- 도메인 / 유스케이스: `.claude/ai-context/domain-overview.md`
- 데이터 모델: `.claude/ai-context/data-model.md`
- API 명세: `.claude/ai-context/api-spec.json`
