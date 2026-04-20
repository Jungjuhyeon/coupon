# 인프라 / 서비스 목록

## 서비스

| 서비스 | 디렉토리 | Eureka 이름 | 포트 | 역할 |
|---|---|---|---|---|
| Discovery Server | `discovery-server/` | - | 8761 | Eureka 서비스 레지스트리 |
| API Gateway | `api-gateway/` | - | 9001 | 외부 진입점, JWT 검증, 라우팅 |
| Member | `member-service/` | `MEMBERSERVER` | 8081 | 회원 가입/로그인/인증 |
| Store | `store-service/` | `STORESERVER` | 8082 | 가게/메뉴 관리 |
| Coupon | `coupon-service/` | `COUPONSERVER` | 8083 | 쿠폰/프로모션, 선착순 발급 |
| Order | `order-service/` | `ORDERSERVER` | 8084 | 주문 생성/조회, Saga 오케스트레이션 |
| Payment | `payment-service/` | `PAYMENTSERVER` | 8085 | 결제 처리 (내부 전용) |
| Common | `common/` | - | - | 공통 예외, 응답 래퍼, BaseEntity |

## 데이터 저장소

| 서비스 | MySQL | MongoDB | Redis Cluster |
|---|---|---|---|
| member-service | O | - | - |
| store-service | O | - | O |
| coupon-service | O | - | O (재고·발급 관리) |
| order-service | O | O (읽기 모델) | O |
| payment-service | O | - | O |

Redis Cluster: 6노드(`redis-node-1~6`), 분산 락은 Redisson 사용

## 기동 순서

```bash
# 1. 인프라
docker network create coupons-network
cd infra && docker-compose -f compose.yml up -d        # Kafka, Redis Cluster
cd {service} && docker-compose -f compose.yml up -d    # 서비스별 MySQL

# 2. 앱 (순서 중요)
./gradlew :discovery-server:bootRun
./gradlew :api-gateway:bootRun
./gradlew :{service}:bootRun                           # 나머지 순서 무관
```

## 빌드 / 테스트

```bash
./gradlew :{service}:build
./gradlew :{service}:test --tests "com.example.SomeTest"
```
