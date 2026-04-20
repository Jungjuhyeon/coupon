# api-gateway

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- Spring Cloud Gateway (WebFlux 기반, reactive)
- 모든 외부 요청의 진입점: JWT 검증 + 라우팅
- Eureka 클라이언트로 등록 (`lb://` 로드밸런싱)
- Port: `9001`

---

## 🔐 JWT 인증 필터 (GlobalFilter, order=-1)

**화이트리스트** (JWT 검증 없이 통과):
- `POST /api/v1/members/login`
- `POST /api/v1/members/signup`

**그 외 모든 경로** → `Authorization: Bearer {token}` 필수
- 검증 알고리즘: HMAC512
- 검증 성공 시 downstream 헤더 추가:
  - `X-USER-ID`: JWT `id` 클레임 (Long)
  - `X-USER-ROLE`: JWT `roles` 클레임 (String)

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- `/internal/**` 경로는 Gateway를 통해 라우팅되나 외부에서 직접 호출 불가 (인프라 레벨 차단)
- 라우팅 전체 테이블: `../.claude/ai-context/api-gateway-spec.json`
