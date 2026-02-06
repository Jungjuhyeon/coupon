# coupon 프로젝트 개요

 대규모 트래픽 선착순 쿠폰 발행 및 주문시스템에 대한 개인 프로젝트입니다.

# 프로젝트 기능
[쿠폰 발급]
1. 하루에 한 사용자당 쿠폰은 1개만 발급 가능합니다.
2. 쿠폰 발행은 특정 이벤트 기간에 제한됩니다.
3. 쿠폰은 이벤트 기간 동안 매일 특정 시간에 시작하여 일정 기간 동안만 발행됩니다.
    * 예시: 오후 1시에 시작하여 1시간 동안 발행됩니다.
4. 쿠폰의 하루당 발행 가능한 최대 수량이 정해져 있습니다.
    * 예시: 쿠폰의 최대 발행 수량은 하루에 5000개입니다.
5. 쿠폰은 이벤트 기간 종료 후 특정 기간 동안만 사용 가능합니다.
    * 예시: 이벤트 기간 종료 후 50% 할인 쿠폰은 1달 이내에 사용되어야 합니다
  
[주문 시스템]
1. 가게등록
2. 메뉴등록
3. 가게정보조회
4. 가게주문
5. 주문목록조회
6. 주문상세조회 

# ERD
<img width="2000" height="1692" alt="image" src="https://github.com/user-attachments/assets/b182ddb9-6e35-4080-aa4a-57629cb0ebf1" />

# 시스템 아키텍처
<img width="815" height="694" alt="image" src="https://github.com/user-attachments/assets/ba97ee05-cb6d-4f9b-a3a3-723a821a23c8" />

## 트러블 슈팅

- [@TransactionalEventListener(AFTER_COMMIT) 에서 왜 Update가 되지않는걸까?](https://velog.io/@wngus4278/Spring-TransactionalEventListenerAFTERCOMMIT-%EC%97%90%EC%84%9C-%EC%99%9C-Update%EA%B0%80-%EB%90%98%EC%A7%80%EC%95%8A%EB%8A%94%EA%B1%B8%EA%B9%8C)
- [Bulk Insert시 JPA의 IDENTITY 문제](https://velog.io/@wngus4278/Kafka-%EC%BB%A8%EC%8A%88%EB%A8%B8-%EB%A1%9C%EA%B7%B8-%EC%B2%98%EB%A6%AC-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%EA%B8%B0-Bulk-Insert%EC%8B%9C-JPA%EC%9D%98-IDENTITY-%EB%AC%B8%EC%A0%9C)
- [Redis Cluster 구성하기](https://velog.io/@wngus4278/Redis-Cluster-%EA%B5%AC%EC%84%B1%ED%95%98%EA%B8%B0)

## MSA 전환기

- [[MSA] 모놀리식 to MSA 전환기 (1) - MSA란](https://velog.io/@wngus4278/Temp-Title-yfdw2gwu)
- [[MSA] 모놀리식 to MSA 전환기 (2) - 멀티 모듈 구성하기](https://velog.io/@wngus4278/MSA-%EB%AA%A8%EB%86%80%EB%A6%AC%EC%8B%9D-to-MSA-%EC%A0%84%ED%99%98%EA%B8%B0-%EB%A9%80%ED%8B%B0-%EB%AA%A8%EB%93%88-%EA%B5%AC%EC%84%B1%ED%95%98%EA%B8%B0)
- [[MSA] 모놀리식 to MSA 전환기 (3) - Service Discovery 패턴 적용하기](https://velog.io/@wngus4278/MSA-%EB%AA%A8%EB%86%80%EB%A6%AC%EC%8B%9D-to-MSA-%EC%A0%84%ED%99%98%EA%B8%B0-Service-Discovery-%ED%8C%A8%ED%84%B4-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
- [[MSA] 모놀리식 to MSA 전환기 (4) - API Gateway 적용하기](https://velog.io/@wngus4278/MSA-%EB%AA%A8%EB%86%80%EB%A6%AC%EC%8B%9D-to-MSA-%EC%A0%84%ED%99%98%EA%B8%B0-API-Gateway-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
- [[MSA] 모놀리식 to MSA 전환기 (5) - 서비스간 통신](https://velog.io/@wngus4278/MSA-%EB%AA%A8%EB%86%80%EB%A6%AC%EC%8B%9D-to-MSA-%EC%A0%84%ED%99%98%EA%B8%B0-d)
## Commit Convention

- feat : 새로운 기능 추가  
- fix : 버그 수정  
- docs : 문서 수정  
- refactor : 코드 리팩토링  
- test : 테스트 코드, 리팩토링 테스트 코드  
