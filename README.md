# SpringbootTemplate

Spring Boot 기반 API 서버를 빠르게 시작하기 위한 템플릿 프로젝트입니다.  
공통 응답 포맷, 전역 예외 처리, JWT 기반 인증 흐름, Swagger(OpenAPI) 문서화, HTTP 로깅 필터, Correlation ID 필터, MapStruct, QueryDSL 의존성 구성을 포함하고 있습니다.

현재 저장소에는 `Spring Boot 3.1.5`, `Java 17`, `Gradle`, `Spring Security`, `Spring Data JPA`, `Validation`, `springdoc-openapi`, `JJWT`, `MapStruct`, `QueryDSL` 설정이 포함되어 있으며, 기본 예제로 `auth` 로그인 API와 `sample` CRUD API가 들어 있습니다.

---

## 1. 프로젝트 목적

이 템플릿은 아래 목적에 맞춰 구성되어 있습니다.

- 신규 Spring Boot 프로젝트의 초기 보일러플레이트 축소
- 인증/인가, 공통 응답, 예외 처리, 로깅 구조의 표준화
- Swagger를 통한 API 명세 및 테스트 편의성 확보
- DTO 매핑(MapStruct), QueryDSL 확장을 고려한 기본 빌드 구성 제공
- 팀 내 공통 백엔드 스타터 프로젝트로 재사용 가능하도록 구조화

---

## 2. 기술 스택

| 구분 | 내용 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.1.5 |
| Build | Gradle (Groovy DSL) |
| Security | Spring Security, JWT (jjwt 0.11.5) |
| Validation | Spring Validation |
| ORM | Spring Data JPA, Hibernate |
| DB | H2 (기본), MariaDB / Oracle 전환 가능 |
| API Docs | springdoc-openapi 2.2.0 |
| Mapping | MapStruct 1.5.5.Final |
| Query | QueryDSL 5.0.0 |
| Logging | SLF4J, MDC 기반 Correlation ID, HTTP Logging Filter |

---

## 3. 주요 기능

### 3-1. 공통 응답 포맷

모든 API 응답은 `BaseResponse<T>` 구조를 사용합니다.

```json
{
  "code": "1000",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {}
}
```

### 3-2. 전역 예외 처리

`GlobalExceptionHandler`를 통해 공통 예외 응답을 내려줍니다.

- `ApiException`
- `MethodArgumentNotValidException`
- `ConstraintViolationException`
- `NoSuchElementException`
- `Exception`

### 3-3. JWT 인증

- `/auth/login` 호출 시 Access Token 발급
- `TokenAuthenticationFilter`에서 Authorization Bearer Token 검증
- 인증 제외 경로 외에는 인증 필요

### 3-4. Swagger(OpenAPI)

- Swagger UI 제공
- Bearer Token 입력 후 보호 API 테스트 가능

### 3-5. 로깅

- `CorrelationIdFilter`를 통해 요청 단위 traceId 생성 및 MDC 저장
- `HttpLoggingFilter`를 통해 요청/응답 로그, 응답 시간, body 로깅
- 민감 정보(password, token 등) 마스킹 로직 포함

### 3-6. DTO 매핑 / QueryDSL 준비

- `MapStruct` 기반 DTO 변환 구조 포함
- `QueryDSL` annotation processor 설정 완료

---

## 5. 실행 방법

### 5-1. 요구 사항

- JDK 17+
- Gradle 8.x 권장

### 5-2. 프로젝트 실행

```bash
git clone https://github.com/YalySeung/SpringbootTemplate.git
cd SpringbootTemplate
./gradlew bootRun
```

Windows 환경:

```bash
gradlew.bat bootRun
```

### 5-3. 빌드

```bash
./gradlew clean build
```

---

## 6. 환경 설정

기본 설정은 `src/main/resources/application.properties`에 있습니다.

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
app.security.jwt.alg=HS256
app.security.jwt.access-token-ttl-seconds=1800
```

### 주요 설정 항목

| 속성 | 설명 |
|---|---|
| `server.port` | 서버 포트 |
| `spring.datasource.*` | 데이터소스 설정 |
| `spring.jpa.*` | JPA/Hibernate 설정 |
| `springdoc.*` | Swagger/OpenAPI 경로 |
| `app.logging.http.max-body-length` | HTTP body 로깅 최대 길이 |
| `app.security.jwt.*` | JWT 알고리즘, issuer, audience, secret, ttl 설정 |

> 운영 환경에서는 `app.security.jwt.secret`를 소스에 직접 고정하지 말고 환경변수 또는 별도 프로파일로 분리하는 것을 권장합니다.

---

## 7. Swagger / OpenAPI

애플리케이션 실행 후 아래 URL에서 문서를 확인할 수 있습니다.

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

보호 API 테스트 절차:

1. `/auth/login`으로 토큰 발급
2. Swagger 우측 상단 `Authorize` 클릭
3. `Bearer {accessToken}` 형식으로 입력
4. 보호 API 호출

---

## 8. 인증 흐름

```text
[Client]
   │
   ├─ POST /auth/login
   │      └─ JwtTokenProvider → Access Token 발급
   │
   └─ Authorization: Bearer <token>
          │
          └─ TokenAuthenticationFilter
                  └─ JwtTokenVerifier
                         └─ SecurityContext 인증 객체 저장
```

---

## 9. 로깅 흐름

### Correlation ID

각 요청마다 traceId를 생성하여 MDC에 저장하고, 요청/응답 로그를 하나의 traceId로 추적할 수 있도록 구성되어 있습니다.

### HTTP Logging

다음 항목을 로깅합니다.

- HTTP Method
- URI / QueryString
- Request Parameter
- Request Body
- Response Status
- Response Body
- Elapsed Time

민감 정보는 단순 마스킹 처리됩니다.

- `authorization`
- `access_token`
- `refresh_token`
- `password`

---

## 10. QueryDSL / MapStruct

### QueryDSL

`build/generated/querydsl` 경로에 QClass가 생성되도록 설정되어 있습니다.

```gradle
implementation "com.querydsl:querydsl-jpa:${querydslVersion}:jakarta"
annotationProcessor "com.querydsl:querydsl-apt:${querydslVersion}:jakarta"
```

### MapStruct

`SampleMapper` 예제로 DTO 매핑 구조가 포함되어 있습니다.

```java
@Mapper
public interface SampleMapper {
    SampleDto toDto(SampleRequest request);
    SampleRequest toRequest(SampleDto sampleDto);
}
```

---

## 11. 라이선스

Apache-2.0

---

## 12. 참고

이 프로젝트는 학습용/사내용 스타터 템플릿으로 사용하기 좋은 구조를 목표로 하며, 실서비스 적용 전에는 보안, 예외 처리, 패키지 구조, 테스트 코드를 프로젝트 성격에 맞게 보강하는 것을 권장합니다.
