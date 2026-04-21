---
name: issue-to-pr
description: |
  GitHub Issue를 기반으로 브랜치 생성 → 코드 구현 → 테스트 작성 → 테스트 실행 → 커밋 → PR 생성까지 전체 플로우를 자동으로 수행하는 스킬.
  다음 상황에서 반드시 이 스킬을 사용한다:
  - "이슈 기반으로 PR 만들어줘", "issue-to-pr", "/issue-to-pr" 라고 요청할 때
  - GitHub Issue URL이 주어지고 구현을 요청할 때
  - "이슈 보고 브랜치 따고 PR 올려줘" 같은 end-to-end 자동화 요청이 올 때
  - 이슈 내용을 직접 붙여넣으며 "이거 구현해서 PR까지 만들어줘"라고 할 때
---

# Issue → PR 자동화 스킬

## 개요

GitHub Issue를 입력받아 아래 순서로 자동 실행한다.

```
Issue 분석 → 브랜치 생성 → 코드 구현 → 테스트 작성 → 테스트 실행 → 커밋 → PR 생성
```

**필수 참조 문서** (작업 전 반드시 읽을 것):
- `.claude/ai-context/architecture.md` — 헥사고날 아키텍처 + 레이어 의존성 규칙
- `.claude/ai-context/conventions.md` — 네이밍, 패키지 경로, Feign 규칙
- `.claude/ai-context/common-policy.md` — 응답 형식, 예외 처리, 인증 규칙
- `CLAUDE.md` — 절대 위반 금지 규칙

---

## Step 1 — Issue 분석

**입력이 URL인 경우** (예: `https://github.com/org/repo/issues/42`):
```bash
# URL에서 issue 번호 추출 후 조회
gh issue view {issue-number} --repo {owner}/{repo} --json title,body,labels,number
```

**입력이 텍스트인 경우:** 그대로 내용을 파싱한다.

분석 시 결정해야 할 항목:

| 항목 | 판단 기준 |
|---|---|
| `type` | feat / fix / refactor / test / chore / docs / style |
| `domain` | issue 내용에서 영향받는 도메인명 (coupon, order, member 등) |
| `action` | 핵심 동작을 동사-명사로 (issue, cancel, validate 등) |
| `target-service` | MSA 서비스 중 어느 서비스에 구현할지 (coupon-service, order-service 등) |

분석 결과를 작업 시작 전에 사용자에게 한 줄로 확인받는다:
> "feat-42/coupon-issue 브랜치로 coupon-service에 구현 예정입니다. 맞으면 진행할게요."

---

## Step 2 — 브랜치 생성

**브랜치 명명 규칙:** `{type}-{issue-number}/{domain}-{action}`

```bash
git checkout develop
git pull origin develop
git checkout -b {type}-{issue-number}/{domain}-{action}
```

예시:
- `feat-42/coupon-issue`
- `fix-17/order-cancel`
- `refactor-8/member-auth`

---

## Step 3 — 코드 구현

### 3-1. 구현 전 확인사항

- 대상 서비스의 `{service}/.claude/CLAUDE.md` 가 있으면 읽는다
- 기존 패턴 파악: 동일 서비스의 유사 UseCase / InputPort / OutputPort 코드를 먼저 읽는다
- 레이어 의존성 방향을 반드시 지킨다:

```
framework/web → usecase(인터페이스) → inputport → outputport(인터페이스) → infra
                                          ↓
                                       domain/model
```

### 3-2. 구현 순서 (헥사고날 아키텍처 기준)

1. `domain/model` — Entity / Value Object / Domain Event (필요 시)
2. `application/outputport` — Outgoing Port 인터페이스
3. `application/usecase` — Incoming Port 인터페이스
4. `application/inputport` — Use Case 구현체
5. `infra/` — OutputPort 구현 (persistence / redis / feign 등)
6. `framework/web` — Controller + Request/Response DTO (payment-service는 `controller/` 패키지 사용)

### 3-3. 절대 금지 규칙

- `domain` 레이어에 Spring, JPA 외 외부 의존성 추가 금지
- `application`에서 `infra` 직접 import 금지 → outputport 인터페이스만 사용
- `framework/web`에서 `inputport` 직접 import 금지 → usecase 인터페이스만 사용
- 도메인 객체 `new` 직접 생성 금지 → `{Entity}.create()` factory method 사용
- `cancleOrder` 오탈자 수정 금지 (order-service 보상 메서드명 — 의도적 네이밍)

---

## Step 4 — 테스트 코드 작성

**테스트 없이 Step 5로 진행 금지.**

### 4-1. 패키지 구조

```
unit/domain/       ← 순수 도메인 로직 (Spring/Mock 없음)
unit/application/  ← UseCase 흐름 (Mockito)
integration/       ← 실제 인프라 (Testcontainers)
```

### 4-2. 필수 작성 기준

| 변경 유형 | 테스트 |
|---|---|
| 도메인 메서드 추가/변경 | `unit/domain` |
| UseCase(InputPort) 추가/변경 | `unit/application` (성공 + 실패) |
| 커스텀 JPQL / FK 관계 | `integration` JPA |
| Redis Lua / 원자적 연산 UseCase | `integration` Redis |
| Kafka 발행 UseCase | `integration` Kafka |

### 4-3. Mock 사용 기준

| 대상 | 방법 |
|---|---|
| OutputPort | `@Mock` (Mockito) |
| 도메인 객체 | `Entity.create()` 또는 `.builder()` — `mock()` 금지 |
| DTO (`@Getter`만 있는 경우) | `new DTO() + ReflectionTestUtils.setField()` — `mock()` 금지 |
| JPA `@GeneratedValue` id | `ReflectionTestUtils.setField(obj, "id", 1L)` |
| Feign / 외부 연동 | `@MockBean` |

### 4-4. Integration 인프라 설정

**공통**
- `@SpringBootTest(webEnvironment = NONE)` + `@Testcontainers` + `@DynamicPropertySource`
- 관심 밖 의존성은 `@MockBean`으로 대체

**JPA**
- `@DataJpaTest` + MySQLContainer
- `save()` 후 `flush()` + `clear()` 필수 (1차 캐시 우회)
- `@EnableJpaAuditing` 테스트 클래스에 선언 금지

**Redis** (Redisson 클러스터 충돌 시 `@SpringBootTest` 포기)
- Redisson 충돌 시: `@SpringBootTest` 없이 `LettuceConnectionFactory`로 직접 빈 조립
- `@BeforeEach`에서 `redisTemplate.getConnectionFactory().getConnection().flushAll()` (테스트 격리)

**Kafka**
- `KafkaContainer` + `MySQLContainer` (+ `MongoDBContainer` 필요 시)
- Redis 미사용 흐름: `@MockBean RedisConnectionFactory` + `@MockBean ReactiveRedisConnectionFactory` + `management.health.redis.enabled=false`
- 검증: 별도 group-id의 `KafkaConsumer`로 구독, `@TransactionalEventListener(AFTER_COMMIT)` 고려해 충분한 대기시간 설정

### 4-5. 공통 원칙

- given / when / then 구조, `@DisplayName` 한국어 명시
- `BusinessException` 검증: `assertThatThrownBy` + `getErrorCode()` 비교

---

## Step 5 — 테스트 실행

```bash
cd {target-service}
./gradlew test
```

**실패 시 처리:**
1. 실패 로그를 읽고 원인 파악
2. 코드 또는 테스트 수정 후 재실행
3. 최대 3회 반복

**3회 시도 후에도 실패하면 즉시 중단:**
- 사용자에게 실패한 테스트명, 에러 메시지, 시도한 수정 내용을 보고한다
- Step 6(커밋) 이후 단계로 절대 진행하지 않는다
- 사용자의 명시적 지시 없이 커밋 및 PR 생성 금지

---

## Step 6 — 커밋

```bash
# 변경 파일 확인 후 명시적으로 add (-A 또는 . 사용 금지)
git status
git add {변경된 파일 목록}
git commit -m "{type}: {변경 내용 한 줄 요약}"
```

**커밋 메시지 규칙:**

| type | 사용 시점 |
|---|---|
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| refactor | 기능 변경 없는 코드 개선 |
| test | 테스트 코드 추가/수정 |
| chore | 빌드, 설정, 패키지 수정 |
| docs | 문서 수정 |
| style | 포맷팅, 세미콜론 등 코드 변경 없는 경우 |

예시:
```
feat: 쿠폰 발급 기능 추가
fix: 주문 취소 시 재고 복구 누락 버그 수정
refactor: 회원 인증 필터 책임 분리
```

---

## Step 7 — PR 생성

```bash
git push origin {브랜치명}

gh pr create \
  --base develop \
  --title "[{TYPE}] {이슈 요약}" \
  --body "$(cat <<'EOF'
## 작업 내용
- {변경 사항 bullet point}

## 테스트 내용
- 성공 케이스: {테스트명}
- 실패 케이스: {테스트명}

## 관련 이슈
Closes #{issue-number}
EOF
)"
```

**PR 제목 형식:** `[{TYPE}] {이슈 요약}` (TYPE은 대문자)

예시:
- `[FEAT] 쿠폰 선착순 발급 API 구현`
- `[FIX] 주문 취소 시 쿠폰 복구 누락 버그 수정`

---

## Step 8 — 결과 출력

작업 완료 후 아래 형식으로 보고한다:

```
## 완료 보고

**브랜치:** feat-42/coupon-issue

**변경 파일:**
- coupon-service/src/main/java/.../usecase/IssueCouponUseCase.java
- coupon-service/src/main/java/.../inputport/IssueCouponInputPort.java
- coupon-service/src/test/java/.../IssueCouponInputPortTest.java
- ...

**테스트 결과:** 성공 (총 N개 — 성공 케이스 X개, 실패 케이스 Y개)

**PR:** {PR URL}
```

---

## 체크리스트 (PR 생성 전 자가 검증)

- [ ] `unit/domain` 테스트 작성 (도메인 비즈니스 메서드 변경 시)
- [ ] `unit/application` 테스트 작성 (성공 + 실패 케이스 모두)
- [ ] `integration` 테스트 작성 (커스텀 JPQL / FK 관계 변경 시)
- [ ] 통합 테스트에 `flush()` + `clear()` 적용 (1차 캐시 우회)
- [ ] 테스트 클래스에 `@EnableJpaAuditing` 미사용 확인
- [ ] `./gradlew test` 통과
- [ ] `domain` 레이어에 외부 의존성 없음
- [ ] `application`에서 `infra` 직접 참조 없음
- [ ] `framework/web`에서 `inputport` 직접 참조 없음
- [ ] 도메인 객체 factory method 사용 (`new` 직접 생성 없음)
- [ ] 커밋 메시지 컨벤션 준수
- [ ] PR base branch가 `develop`으로 설정되어 있음
- [ ] PR 내용에 Issue 링크(`Closes #N`) 포함
