# 공통 규칙 및 정책

## 응답 형식 (common 모듈)

모든 API 응답은 `SuccessResponse<T>` 래퍼를 사용한다.

```java
SuccessResponse.success(data)               // 데이터 있는 성공 응답
SuccessResponse.successWithoutResult("메시지") // 데이터 없는 성공 응답
```

내부 Feign 호출(`/internal/**`)은 래퍼 없이 원시 타입/DTO를 직접 반환한다.

---

## 예외 처리

- 모든 비즈니스 예외는 `BusinessException`(common 모듈)을 상속한다.
- 에러 코드는 `CommonErrorCode` 또는 서비스별 ErrorCode enum으로 정의한다.
- 글로벌 예외 핸들러가 `BusinessException`을 적절한 HTTP 상태코드로 변환한다.

---

## 인증/인가

**게이트웨이 JWT 필터 (`JwtAuthenticationFilter` — GlobalFilter, order=-1)**
- 화이트리스트(인증 불필요): `/api/v1/members/login`, `/api/v1/members/signup`
- 나머지 모든 요청: JWT 검증 후 `X-USER-ID`, `X-USER-ROLE` 헤더를 추가해 다운스트림 전달

**서비스별 필터 (`AuthenticationContextFilter`)**
- `X-USER-ID`, `X-USER-ROLE` 헤더를 읽어 `AuthPrincipal` 생성 → `SecurityContextHolder` 등록
- 컨트롤러에서 `@AuthenticationPrincipal AuthPrincipal`로 주입받음

```java
@AuthenticationPrincipal AuthPrincipal principal
Long memberId = principal.getId();
String role   = principal.getRole();
```

## 엔티티 기본 구조

모든 JPA 엔티티는 `BaseEntity`(common 모듈)를 상속한다.

```java
// BaseEntity 제공 필드
LocalDateTime createdAt
LocalDateTime updatedAt
```

---

## 데이터베이스 규칙

- 각 서비스는 자신의 DB만 접근 (DB per service)
- 다른 서비스 데이터 필요 시 반드시 Feign 또는 Kafka 이벤트를 통해 조회
- DB 구성 상세 → `.claude/ai-context/infra.md`
