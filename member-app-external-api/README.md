# Application System (Member-External-API)

## 어플리케이션 모듈 계층 (회원 API)

- 클라이언트가 사용 가능한 회원 API

### 1.역할과 책임

![image](https://user-images.githubusercontent.com/70932170/225805178-1c552a7c-9e29-4af7-9559-599d3fec2c55.png)

- 독립적으로 실행 가능한 애플리케이션 모듈 계층
- 비지니스 로직에 맞게 하위 모듈들을 조립하는 역할

### 2. 실행 방법 (사용 방법)

- 사용 가능한 모듈
    - 애플리케이션 모듈 (`xxx-app-yyy`)
    - 내부 모듈 (`core-module`)
    - 도메인 모듈 (`xxx-domain-yyy`)
    - 공통 모듈 (`common-module`)
    - 독립 모듈 (`modules`)

| 애플리케이션 모듈    | 내부 모듈 | 도메인 모듈 | 공통 모듈 | 독립 모듈 |
|--------------|-------|--------|-------|-------|
| 사용 가능한 모듈 여부 | O     | O      | O     | O     |

### 3. 관례

```bash
# xxx-app-yyy
- xxx-app-batch
- xxx-app-worker
- xxx-app-external-api
- xxx-app-internal-api
```