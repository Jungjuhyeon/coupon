# store-service — Domain + UseCase

---

## Domain

### Aggregate Root: Store

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long (PK, IDENTITY) | 가게 식별자 |
| brand | Brand (ManyToOne, LAZY) | 브랜드 |
| storeCategory | StoreCategory (ManyToOne, LAZY) | 가게 카테고리 (업종) |
| ownerId | Long | 점주 회원 ID (member-service 참조) |
| menuList | List\<Menu\> (OneToMany, CascadeALL) | 메뉴 목록 |
| name | String | 가게명 |
| storePhoneNumber | String | 가게 전화번호 |
| address | String | 주소 |
| status | StoreStatus | 영업 상태 |

**StoreStatus enum**: `OPEN`(기본값) / `CLOSED` / `SUSPENDED` / `DELETED`

```java
Store.create(Brand brand, StoreCategory storeCategory, Long ownerId,
             String name, String phone, String address)
// → status = StoreStatus.OPEN
```

### Child Entity: Menu

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long (PK, IDENTITY) | 메뉴 식별자 |
| store | Store (ManyToOne, LAZY) | 소속 가게 |
| menuCategory | MenuCategory (ManyToOne, LAZY) | 메뉴 카테고리 |
| name | String | 메뉴명 |
| price | Integer | 가격 |
| menuStatus | MenuStatus | 판매 상태 |

**MenuStatus enum**: `AVAILABLE`(기본값) / `UNAVAILABLE` / `HIDDEN` / `DELETED`

```java
Menu.create(Store store, MenuCategory menuCategory, String name, Integer price)
// → menuStatus = MenuStatus.AVAILABLE
```

### 참조 엔티티 (사전 등록 데이터, factory method 없음)

| 엔티티 | 필드 | 설명 |
|---|---|---|
| `StoreCategory` | id, name | 가게 업종 (예: 한식, 중식) |
| `Brand` | id, storeCategory, name | 브랜드 |
| `MenuCategory` | id, storeCategory, name | 업종별 메뉴 카테고리 (예: 메인, 사이드) |

---

## UseCase (Incoming Port)

| 인터페이스 | 구현체 | 설명 |
|---|---|---|
| `AddStoreUseCase` | `AddStoreInputPort` | 가게 등록 |
| `AddMenuUseCase` | `AddMenuInputPort` | 메뉴 일괄 등록 |
| `InquiryStoreUseCase` | `InquiryStoreInputPort` | 가게 조회, order-view, 존재 확인 |
| `InquiryMenuUseCase` | `InquiryMenuInputPort` | 메뉴 가격 목록 조회 (Feign용) |

### 주요 흐름

**AddStoreInputPort.create(memberId, StoreInfoDTO)**
1. `storeCategoryOutputPort.findById()` → StoreCategory 조회
2. `brandOutputPort.findById()` → Brand 조회
3. `memberOutputPort.existsOwner(memberId)` → Feign MEMBERSERVER 점주 확인
4. `Store.create()` → `storeOutputPort.save()`

**AddMenuInputPort.create(MenuListDTO)**
1. `storeOutputPort.findById()` → Store 조회
2. 메뉴 목록 순회 → `menuCategoryOutputPort.findById()` → `Menu.create()`
3. `menuOutputPort.saveAll(menuList)`

**InquiryStoreInputPort**
- `getStore()` → Store 조회 + `memberOutputPort.getOwnerInfo()` (Feign) → `StoreInfoOutputDTO`
- `getStoreOrderView()` → 가게 기본 정보 + 메뉴명 → `StoreOrderViewFeignDTO` (order-service용)
- `existsById()` → boolean (order-service Feign용)

### Outgoing Port

| 인터페이스 | 구현체 | 역할 |
|---|---|---|
| `StoreOutputPort` | `StoreAdapter` | 가게 저장/조회 |
| `MenuOutputPort` | `MenuAdapter` | 메뉴 저장/조회 |
| `BrandOutputPort` | `BrandAdapter` | 브랜드 조회 |
| `StoreCategoryOutputPort` | `StoreCategoryAdapter` | 가게 카테고리 조회 |
| `MenuCategoryOutputPort` | `MenuCategoryAdapter` | 메뉴 카테고리 조회 |
| `MemberOutputPort` | `MemberClientAdapter` | Feign MEMBERSERVER: `GET /internal/members/{memberId}` → boolean (existsOwner), `GET /internal/members/profile/{memberId}` → MemberProfileFeignDTO (getOwnerInfo) |
