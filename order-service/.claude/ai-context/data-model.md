# order-service 데이터 모델

## MySQL 테이블

### orders
```sql
CREATE TABLE orders (
    order_id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT NOT NULL,
    store_id         BIGINT NOT NULL,
    coupon_issue_id  BIGINT,                          -- nullable, 쿠폰 미사용 시 null
    original_price   INT,
    discount_value   INT,
    final_price      INT,
    delivery_address VARCHAR(255),
    order_time       DATETIME(6),
    comment          VARCHAR(255),
    order_status     VARCHAR(20) NOT NULL,            -- PENDING|ACCEPTED|IN_PROGRESS|READY|COMPLETED|CANCELLED|FAILED|REFUNDED
    created_at       DATETIME(6),
    updated_at       DATETIME(6)
);
```

### order_menu
```sql
CREATE TABLE order_menu (
    order_menu_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT NOT NULL,                   -- FK → orders
    menu_id        BIGINT NOT NULL,                   -- store-service 메뉴 ID
    quantity       INT,
    price          INT,                               -- 단가
    total_price    INT,                               -- 단가 × 수량
    order_status   VARCHAR(20) NOT NULL,              -- Order와 동기화
    created_at     DATETIME(6),
    updated_at     DATETIME(6)
);
```

### outbox_event
```sql
CREATE TABLE outbox_event (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    aggregate_type VARCHAR(255),                      -- "ORDER"
    aggregate_id   BIGINT,                            -- orderId
    event_type     VARCHAR(255),
    payload        LONGTEXT,                          -- JSON 직렬화된 OrderCreatedEvent
    created_at     DATETIME(6),
    status         VARCHAR(30) NOT NULL               -- READY_TO_PUBLISH|PUBLISHED|MESSAGE_CONSUME|FAILED
);
```

## MongoDB 컬렉션

### order_read_models
CQRS 읽기 전용 모델. 주문 생성 시 upsert, Saga 결과 수신 시 상태 업데이트.

```json
{
  "_id": "ObjectId",
  "memberId": 1,
  "orderId": 100,
  "originalPrice": 20000,
  "discountAmount": 2000,
  "finalPrice": 18000,
  "orderTime": "2025-04-01T12:00:00",
  "storeCategoryName": "한식",
  "storeBrandName": "한솥",
  "storeName": "한솥 강남점",
  "orderStatus": "COMPLETED",
  "orderMenuList": [
    {
      "orderMenuId": 1,
      "menuId": 10,
      "name": "도시락A",
      "price": 10000,
      "quantity": 2,
      "totalPrice": 20000
    }
  ]
}
```

**인덱스**:
- `{memberId: 1, orderTime: -1}` — 내 주문 목록 정렬 조회
- `{orderId: 1}` unique — orderId로 단건 조회/업데이트
