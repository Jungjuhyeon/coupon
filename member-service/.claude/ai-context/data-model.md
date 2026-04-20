# member-service 데이터 모델

## MySQL

### members 테이블

```sql
CREATE TABLE members (
    member_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    phone       VARCHAR(255),
    role        VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);
```
