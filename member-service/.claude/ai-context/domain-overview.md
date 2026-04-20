# member-service — Domain + UseCase

---

## Domain

### Aggregate: Member

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long (PK, IDENTITY) | 회원 식별자 |
| email | String | 이메일 (로그인 ID) |
| name | String | 이름 |
| password | String | 암호화된 비밀번호 |
| phone | String | 전화번호 |
| role | Role | 회원 권한 |

**Role enum**: `ADMIN(ROLE_ADMIN)` / `USER(ROLE_USER)` — 생성 시 기본값 `USER`

```java
// 팩토리 메서드 (new 직접 호출 금지)
Member.create(String email, String name, String phone, String password)
// → role = Role.USER 고정
```

---

## UseCase (Incoming Port)

| 인터페이스 | 구현체 | 메서드 |
|---|---|---|
| `AuthMember` | `AuthInputPort` | `signUp(MemberInfoDTO)`, `login(MemberLoginDTO)` |
| `InquiryMemberUseCase` | `InquiryMemberInputPort` | `existsById(Long)`, `getMemberProfile(Long)` |

### 주요 흐름

**signUp**
1. 이메일 중복 확인 (`MemberOutputPort.existsByEmail`)
2. 비밀번호 암호화 (`PasswordEncoder`)
3. `Member.create()` → `MemberOutputPort.save()`

**login**
1. 이메일로 회원 조회 (`MemberOutputPort.findByEmail`)
2. 비밀번호 검증
3. JWT 발급 (`JwtTokenGenerator`) → `MemberLoginOutputDTO` 반환

**existsById / getMemberProfile**
- 다른 서비스의 Feign 호출에 응답하는 내부 전용 유스케이스
