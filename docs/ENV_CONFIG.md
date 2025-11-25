# 환경 변수 설정

로또 시뮬레이터는 마이크로서비스 아키텍처로 구성되어 있으며, 각 서비스의 설정을 환경 변수로 관리합니다. 이를 통해 개발, 테스트, 운영 환경에서 코드 변경 없이 설정을 변경할 수 있습니다.

## 빠른 시작
```bash
# .env 파일이 있는지 확인 후 실행
docker-compose up
```

## 시스템 공통 설정

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `SPRING_PROFILES_ACTIVE` | `local` | Spring Boot 프로필 | 환경별 설정 분리 |

## 서비스별 포트 및 URL

마이크로서비스 간 통신을 위한 포트와 URL 설정입니다. 각 서비스는 독립적인 컨테이너에서 실행되며, 서로 HTTP 통신을 통해 협력합니다.

| 서비스 | 포트 변수 | 기본 포트 | URL 변수 | 기본 URL | 역할 |
|--------|-----------|----------|----------|----------|------|
| 중앙 서버 | `CENTRAL_SERVER_PORT` | `8100` | `CENTRAL_SERVER_URL` | `http://central-server:8100` | 로또 라운드 관리, 당첨번호 생성 |
| 봇 클라이언트 | `BOT_CLIENT_PORT` | `8200` | - | - | 자동 티켓 구매 봇 관리 |
| POS 매니저 | `POS_MANAGER_PORT` | `8300` | `POS_MANAGER_URL` | `http://pos-manager:8300` | POS 터미널 풀 관리 |
| POS 터미널 | `POS_TERMINAL_PORT` | `8400` | - | - | 실제 티켓 판매 처리 |
| 대시보드 | `DASHBOARD_PORT` | `8500` | `DASHBOARD_URL` | `http://dashboard:8500` | 시스템 모니터링 |

## 데이터베이스 설정

PostgreSQL을 사용하여 로또 데이터를 저장합니다. JPA를 통한 ORM 매핑으로 엔티티 관계를 관리합니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `DB_HOST` | `postgres` | DB 호스트명 | 컨테이너 네트워크 내 DB 접근 |
| `DB_PORT` | `5432` | PostgreSQL 포트 | 표준 PostgreSQL 포트 |
| `DB_NAME` | `lotto_db` | 데이터베이스명 | 로또 데이터 저장소 |
| `DB_USER` | `lotto_db_admin` | DB 사용자명 | 애플리케이션 DB 계정 |
| `DB_PASSWORD` | `sXoBgDJjra_kwM8e` | DB 비밀번호 | 운영환경에서 변경 필수 |
| `JPA_DDL_AUTO` | `create` | JPA DDL 옵션 | 테이블 자동 생성/업데이트 |

## 봇 설정

자동 티켓 구매를 수행하는 봇의 동작을 제어합니다. 봇은 실제 사용자의 구매 패턴을 시뮬레이션합니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `BOT_COUNT` | `10` | 생성할 봇 수 | 동시 구매 부하 시뮬레이션 |
| `BOT_STRATEGY` | `RANDOM` | 봇 구매 전략 | RANDOM/FIXED/SMART 전략 |
| `BOT_DEFAULT_POS_COUNT` | `3` | 봇당 POS 터미널 수 | 봇의 구매 채널 다양성 |
| `BOT_DEFAULT_PURCHASE_INTERVAL_MS` | `1000` | 구매 간격(ms) | 구매 빈도 조절 |
| `BOT_DEFAULT_TICKETS_PER_PURCHASE` | `1` | 한 번에 구매할 티켓 수 | 구매 볼륨 조절 |

## 라운드 설정

로또 게임의 라운드 주기를 제어합니다. 실제 로또와 유사한 시간 주기로 게임이 진행됩니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `LOTTO_ROUND_OPEN_DURATION` | `120` | 라운드 열림 시간(초) | 티켓 구매 가능 시간 |
| `LOTTO_ROUND_CLOSED_DURATION` | `10` | 라운드 닫힘 시간(초) | 당첨번호 생성 및 결과 발표 |
| `LOTTO_ROUND_CHECK_INTERVAL` | `500` | 상태 체크 간격(ms) | 라운드 상태 전환 감지 |

## 성능 최적화

시스템 모니터링과 상태 보고 주기를 설정합니다. 실시간 대시보드 업데이트를 위한 설정입니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `BOT_STATUS_INTERVAL` | `5000` | 봇 상태 보고 주기(ms) | 봇 활동 모니터링 |
| `BOT_HEALTH_CHECK_INTERVAL_MS` | `5000` | 봇 헬스체크 주기(ms) | 봇 생존 확인 |
| `POS_MANAGER_HEALTH_CHECK_INTERVAL_MS` | `30000` | POS 헬스체크 주기(ms) | POS 터미널 상태 확인 |
| `SSE_UPDATE_INTERVAL` | `50` | SSE 업데이트 간격(ms) | 실시간 웹 업데이트 |
| `METRICS_COLLECTION_INTERVAL` | `50` | 메트릭 수집 간격(ms) | 성능 지표 수집 |

## 타임아웃 설정

네트워크 통신의 안정성을 위한 타임아웃 설정입니다. 서비스 간 통신 장애를 방지합니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `CONNECTION_TIMEOUT_MS` | `5000` | 연결 타임아웃(ms) | HTTP 연결 대기 시간 |
| `READ_TIMEOUT_MS` | `5000` | 읽기 타임아웃(ms) | 응답 대기 시간 |
| `POS_MANAGER_READ_TIMEOUT` | `5000` | POS 매니저 읽기 타임아웃(ms) | POS 통신 안정성 |

## 컨테이너 설정

Docker 컨테이너 확장성과 용량 제한을 설정합니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `POS_TERMINAL_SCALE` | `3` | POS 터미널 인스턴스 수 | 부하 분산 |
| `POS_MANAGER_MAX_TERMINAL_CAPACITY` | `100` | 최대 관리 터미널 수 | 시스템 확장 한계 |

## 장애 처리

시스템 장애 상황에 대한 임계값과 재시도 정책을 설정합니다.

| 변수명 | 기본값 | 설명 | 용도 |
|--------|--------|------|------|
| `BOT_POS_FAILURE_THRESHOLD` | `5` | POS 장애 임계값 | POS 재할당 트리거 |
| `BOT_POS_ALLOCATION_MAX_RETRY` | `5` | 최대 할당 재시도 횟수 | 할당 실패 방지 |
| `OWNER_VALIDATION_FAILURE_THRESHOLD` | `3` | 소유자 검증 실패 임계값 | 봇-POS 연결 안정성 |

## 환경별 설정 가이드

### 개발 환경
- `.env` 파일의 기본값 사용
- `JPA_DDL_AUTO=create`로 테이블 자동 생성
- 낮은 봇 수와 짧은 라운드 시간으로 빠른 테스트

### 운영 환경
- `DB_PASSWORD` 반드시 변경
- `JPA_DDL_AUTO=validate`로 스키마 검증만 수행
- 높은 봇 수와 실제 로또와 유사한 라운드 시간 설정