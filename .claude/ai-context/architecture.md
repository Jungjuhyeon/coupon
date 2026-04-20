# 헥사고날 아키텍처 + DDD

## 핵심 원칙

- **도메인이 중심** — 외부 기술(DB, Kafka, Redis, Feign)은 도메인을 모른다
- **Ports & Adapters** — 애플리케이션은 포트(인터페이스)를 통해서만 외부와 통신
- **DDD** — Entity / Aggregate Root / Value Object / Domain Event / Factory Method / Domain Service

## 의존성 방향

```
[framework/web] [infra/kafkaadapter]   ← Driving Adapter (외부 진입)
       ↓ 호출
[application/usecase]                  ← Incoming Port (인터페이스)
[application/inputport]                ← Use Case 구현체
[application/service]                  ← Domain Service
[application/outputport]               ← Outgoing Port (인터페이스)
       ↓ 의존              ↓ 구현
[domain/model]        [infra/]         ← Driven Adapter (persistence/redis/kafka/feign)
```

- `domain` → 순수 Java + JPA 어노테이션만, 외부 의존 없음
- `application` → `domain`만 의존, `infra` 직접 참조 금지
- `infra` → `outputport` 인터페이스 구현
- `framework/web` → `usecase` 인터페이스 호출

## 각 레이어 책임

| 레이어 | 책임 |
|---|---|
| `domain/model` | Entity, Aggregate Root, Value Object, Domain Event, Factory Method |
| `application/usecase` | Incoming Port 인터페이스 정의만 |
| `application/inputport` | usecase 구현, outputport + domain service 조합 |
| `application/outputport` | Outgoing Port 인터페이스 정의만 |
| `application/service` | 단일 엔티티로 표현 불가한 도메인 로직 (Validator 등) |
| `infra/persistence` | JPA Repository + Adapter |
| `infra/kafkaadapter` | Kafka Producer / Consumer |
| `infra/redisadapter` | Redis Adapter |
| `infra/{service}` | Feign 클라이언트 + Adapter |
| `framework/web` | Controller + DTO 변환, 비즈니스 로직 없음 |

## 패키지 구조

```
{service}/src/main/java/com/example/{service-name}/
├── domain/model/
│   ├── {Entity}.java          ← Aggregate / Entity
│   ├── cache/                 ← Redis 캐시 VO (비JPA)
│   ├── enumeration/           ← Value Object (enum)
│   └── event/                 ← Domain Event
├── application/
│   ├── usecase/               ← Incoming Port (인터페이스)
│   ├── inputport/             ← Use Case 구현체
│   ├── outputport/            ← Outgoing Port (인터페이스)
│   ├── service/               ← Domain Service (Validator 등)
│   └── scheduler/             ← 재시도/배치 스케줄러
├── infra/
│   ├── persistence/
│   ├── kafkaadapter/
│   ├── redisadapter/
│   └── {externalservice}/
├── framework/web/             ← Controller + DTO
└── auth/                      ← JWT 인증 필터
```

> **예외**: payment-service는 `framework/web/` 대신 `controller/` 패키지 사용

## 코드 예시

```java
// usecase — 인터페이스만
public interface IssueCouponUseCase {
    void issue(Long promotionId, Long couponId, Long memberId);
}

// inputport — usecase 구현, outputport 주입
@Service
public class IssueCouponInputPort implements IssueCouponUseCase {
    private final CouponCacheOutputPort couponCacheOutputPort; // infra 직접 참조 금지
    private final CouponValidator couponValidator;
}

// outputport — 인터페이스만, infra가 구현
public interface CouponCacheOutputPort {
    void saveCouponData(Long couponId, int stock, LocalDate startDate, LocalDate endDate);
    CouponCache getCouponCache(Long couponId);
}
```
