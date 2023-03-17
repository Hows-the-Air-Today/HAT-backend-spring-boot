# Domain System (Air Quality Domain Redis)

## 도메인 모듈 계층 (대기 정보 도메인(Redis))

- Redis를 사용하는 대기 정보 도메인 모듈

### 1.역할과 책임

![image](https://user-images.githubusercontent.com/70932170/225795468-c0b11085-73c9-4a2d-8f2d-123c2989f7d1.png)

- 시스템의 중심 도메인을 다루는 모듈이 위치하는 계층
- `도메인 모듈 계층`은 오로지 도메인에 집중한다.
    - `애플리케이션 계층`의 비지니스 로직을 모른다.
- 하나의 모듈은 최대 하나의 인프라스트럭처에 대한 책임만 갖는다.
    - 의존성의 전파를 방지하기 위함
- `xxx-domain-service`와 같이 도메인 모듈을 조합한 더 큰 단위의 도메인 모듈이 있을 수 있다.
    - ![image](https://user-images.githubusercontent.com/70932170/225802408-40ba14a4-4ec8-4d10-ae53-6fd63c7d2178.png)
- 도메인 모듈은 아래와 같이 `Domain`, `Repository`, `Domain Service`와 같이 세 개의 계층을 가진다.
    - ![image](https://user-images.githubusercontent.com/70932170/225793569-216416e7-2425-4829-9156-147b92e1ca1a.png)
    - `Domain`
        - Java Class로 표현된 도메인 Class들이 존재
            - JPA 기준 테이블과 Mapping되는 Class
        - 도메인 모듈 내부에서 이 계층에 위치한 도메인들을 통해 대화
    - `Repository`
        - 도메인의 `조회`, `저장`, `수정`, `삭제` 역할 수행
            - **주의할 점**
                - 모든 CRUD를 수행하는 것이 아니다.
                - `A` 애플리케이션에서 도메인에 대한 `통계` 기능을 추가한다고 했을 때, `통계`라는 기능이 이 시스템에서 중심 역할로 볼 수 있다면 `도메인 모듈`에 작성하고, 그렇지 않다면
                  사용을 하는 측에 작성되어야 한다.
                - 예를 들어, `A` 시스템이 `주문`이라는 중심 도메인을 갖는다면, `통계`라는 기능은 사용하는 측(`Application Module`)에 작성한다.
    - `Domain Service`
        - 이 계층은 도메인의 비지니스를 책임진다.
            - 도메인이 갖는 비지니스가 단순하다면 이 계층은 생기지 않을 수도 있다.
        - 이 계층에서는 `트랜잭션의 단위`, `요청 데이터 검증`, `이벤트 발생` 등의 도메인 비지니스를 작성한다.

### 2. 실행 방법 (사용 방법)

- 사용 가능한 모듈
    - 도메인 모듈 (`xxx-domain-yyy`)
    - 공통 모듈 (`common-module`)
    - 독립 모듈 (`modules`)

|              | 애플리케이션 모듈 | 내부 모듈 | 도메인 모듈 | 공통 모듈 | 독립 모듈 |
|--------------|-----------|-------|--------|-------|-------|
| 사용 가능한 모듈 여부 | X         | X     | O      | O     | O     |

### 3. 관례

```bash
# xxx-domain-yyy
- xxx-domain-service
- xxx-domain-rds
- xxx-domain-redis
```