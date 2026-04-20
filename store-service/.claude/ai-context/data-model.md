# store-service 데이터 모델

## MySQL 테이블

### store_category
```sql
CREATE TABLE store_category (
    store_category_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(255),                 -- 예: 한식, 중식, 일식
    created_at         DATETIME(6),
    updated_at         DATETIME(6)
);
```

### brand
```sql
CREATE TABLE brand (
    brand_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_category_id  BIGINT NOT NULL,              -- FK → store_category
    name               VARCHAR(255),
    created_at         DATETIME(6),
    updated_at         DATETIME(6)
);
```

### menu_category
```sql
CREATE TABLE menu_category (
    menu_category_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_category_id  BIGINT NOT NULL,              -- FK → store_category (업종별 카테고리)
    name               VARCHAR(255),                 -- 예: 메인, 사이드, 음료
    created_at         DATETIME(6),
    updated_at         DATETIME(6)
);
```

### store
```sql
CREATE TABLE store (
    store_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id           BIGINT NOT NULL,              -- FK → brand
    store_category_id  BIGINT NOT NULL,              -- FK → store_category
    owner_id           BIGINT NOT NULL,              -- member-service 회원 ID
    name               VARCHAR(255) NOT NULL,
    store_phone_number VARCHAR(255),
    address            VARCHAR(255),
    status             VARCHAR(20) NOT NULL,         -- OPEN|CLOSED|SUSPENDED|DELETED
    created_at         DATETIME(6),
    updated_at         DATETIME(6)
);
```

### menu
```sql
CREATE TABLE menu (
    menu_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id           BIGINT NOT NULL,              -- FK → store
    menu_category_id   BIGINT NOT NULL,              -- FK → menu_category
    name               VARCHAR(255),
    price              INT,
    menu_status        VARCHAR(20) NOT NULL,         -- AVAILABLE|UNAVAILABLE|HIDDEN|DELETED
    created_at         DATETIME(6),
    updated_at         DATETIME(6)
);
```

## 엔티티 관계 요약

```
StoreCategory (1) ──< Brand (N)
StoreCategory (1) ──< MenuCategory (N)
Brand (1) ──< Store (N)
StoreCategory (1) ──< Store (N)
Store (1) ──< Menu (N)
MenuCategory (1) ──< Menu (N)
```
