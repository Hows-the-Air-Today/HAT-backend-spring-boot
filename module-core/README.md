# Core System

## 내부 모듈 계층

### 1.역할과 책임

![image](https://user-images.githubusercontent.com/70932170/225807275-2f90ba05-d84b-46e9-8926-e55d3159553f.png)

- 저장소, 도메인 외 시스템에서 필요한 모듈들이 위치하는 계층
- 애플리케이션 비지니스와 도메인 비지니스를 모른다.
- 시스템 전체적인 기능을 서포트하기 위한 기능 모듈 등이 만들어 질 수 있다.
    - Web Filter, Security, Logging 등 웹에 대한 필수적인 공통 설정

### 2. 실행 방법(사용 방법)

- 사용 가능한 모듈
    - 내부 모듈 (`module-core`)
    - 공통 모듈 (`module-common`)
    - 독립 모듈 (`modules`)

|              | 애플리케이션 모듈 | 내부 모듈 | 도메인 모듈 | 공통 모듈 | 독립 모듈 |
|--------------|-----------|-------|--------|-------|-------|
| 사용 가능한 모듈 여부 | X         | O     | X      | O     | O     |

### 3. 관례

```bash
- module-core
- core-web
- xxx-client
- xxx-event-publisher
```