# 코딩 컨벤션

## 네이밍

| 구성요소 | 패턴 | 예시 |
|---|---|---|
| Use Case 인터페이스 | `{Action}UseCase` | `IssueCouponUseCase` |
| Use Case 구현체 | `{Action}InputPort` | `IssueCouponInputPort` |
| Outgoing Port | `{Domain}{Tech}OutputPort` | `CouponCacheOutputPort` |
| Infra 어댑터 | `{Domain}Adapter` | `CouponRedisAdapter` |
| Feign 클라이언트 | `{Service}FeignClient` | `MemberFeignClient` |

## API 경로

- 외부 노출: `/api/v1/**`
- 서비스 간 내부 전용: `/internal/**` (api-gateway 미노출)

## Feign

```java
@FeignClient(name = "EUREKA서비스명")  // 반드시 대문자, api-gateway 미경유
```
