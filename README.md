# Commerce

Spring Boot 기반 커머스 백엔드 애플리케이션입니다.

## 기술 스택

- **Language**: Java 25
- **Framework**: Spring Boot 4.0.6
- **Database**: MySQL 9.7.0 + Flyway
- **ORM**: Spring Data JPA
- **Observability**: OpenTelemetry + Grafana LGTM
- **Docs**: Spring REST Docs
- **Test**: JUnit 5, Testcontainers

## 시작하기

### 사전 요구사항

- JDK 25
- Docker (로컬 실행 시 필요)

### 로컬 실행

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

`local` 프로파일은 Docker Compose를 자동으로 시작합니다. MySQL과 Grafana LGTM 컨테이너가 함께 실행됩니다.

### 빌드

```bash
./gradlew build
```

테스트 실행 → API 문서 생성 → JAR 패키징 순서로 진행됩니다.

## 프로파일

| 프로파일    | 설명                                                                     |
|---------|------------------------------------------------------------------------|
| `local` | 로컬 개발. Docker Compose 자동 시작, SQL 로깅 활성화                                |
| `dev`   | 개발 서버. OTLP 연동                                                         |
| `prod`  | 운영 서버. `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `OTLP_ENDPOINT` 환경변수 필요 |

## API 문서

애플리케이션 실행 후 아래 URL에서 확인할 수 있습니다.

```
http://localhost:8080/docs/index.html
```

문서는 Spring REST Docs로 생성되며, 빌드 시 테스트를 통해 자동으로 갱신됩니다.

## Observability

로컬 환경에서 Grafana 대시보드는 아래 URL에서 접근할 수 있습니다.

```
http://localhost:3000
```

Metrics, Logs, Traces를 Grafana LGTM 스택으로 수집합니다.
