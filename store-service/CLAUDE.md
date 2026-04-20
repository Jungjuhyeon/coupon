# store-service

> 전체 아키텍처/정책 → 루트 `CLAUDE.md` 및 `../.claude/ai-context/`

---

## 📌 서비스 개요
- 가게 등록/조회, 메뉴 관리 담당
- Eureka: `STORESERVER` / Port: `8082`
- DB: MySQL (Store, Menu, Brand, StoreCategory, MenuCategory)

---

## 🔗 서비스 관계

| 방향 | 서비스 | 방식 | 목적 |
|---|---|---|---|
| 호출 | MEMBERSERVER | Feign | 가게 등록 시 점주 존재 확인, 점주 프로필 조회 |
| 피호출 | order-service | Feign | 가게 존재 확인, 가게+메뉴 뷰, 메뉴 가격 조회 |

Kafka 미사용

---

## 📚 참조 문서 (필요 시 명시적으로 읽을 것)

- 도메인 / 유스케이스: `.claude/ai-context/domain-overview.md`
- 데이터 모델: `.claude/ai-context/data-model.md`
- API 명세: `.claude/ai-context/api-spec.json`
- 외부 서비스 호출: `.claude/ai-context/external-integration.md`
