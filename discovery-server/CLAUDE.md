# discovery-server

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- Spring Cloud Netflix Eureka Server
- 모든 마이크로서비스의 서비스 레지스트리
- Port: `8761` / 대시보드: `http://localhost:8761`
- 자기 자신은 Eureka에 등록하지 않음 (`register-with-eureka: false`, `fetch-registry: false`)

---

## 🔗 등록 서비스 목록

| Eureka 등록명 | 서비스 | Port |
|---|---|---|
| `MEMBERSERVER` | member-service | 8081 |
| `STORESERVER` | store-service | 8082 |
| `COUPONSERVER` | coupon-service | 8083 |
| `ORDERSERVER` | order-service | 8084 |
| `PAYMENTSERVER` | payment-service | 8085 |
| `api-gateway` | api-gateway | 9001 |

---

비즈니스 로직 없음. 설정 변경 시 `src/main/resources/application.yml` 참조.
