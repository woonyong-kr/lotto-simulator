# lotto-central-server

실제 로또 서비스의 본사 시스템에 해당하는 중앙 서버입니다. 회차를 열고 닫고, POS에서 올라오는 티켓 발행 요청을 검증하고, 추첨을 수행합니다. 다른 모든 모듈이 이 서버의 REST API에 의존하며, PostgreSQL을 사용해 모든 데이터를 영구 저장합니다.

## 구현된 기능

스케줄러가 환경변수로 설정된 간격마다 가장 최신 회차를 확인하고, OPEN 상태 회차는 설정된 시간이 지나면 CLOSED로 전환합니다. 회차가 종료되면 자동으로 당첨 번호를 추첨하고 모든 티켓의 당첨 여부를 계산합니다. 새 회차는 이전 회차가 CLOSED 상태가 된 후 자동으로 생성됩니다.

티켓 발행은 수동/자동 타입을 지원하며, Pessimistic Lock을 사용해 동시에 여러 요청이 들어와도 회차 상태를 안전하게 확인합니다. 봇과 POS는 중앙 서버에 등록되어 관리되며, 봇 생성 시 자동으로 설정된 개수만큼 POS를 할당합니다. SSE 엔드포인트는 최근 회차 통계를 주기적으로 클라이언트에 전송합니다.

## 주요 API

| 리소스 | 메서드 | 경로 | 설명 |
| --- | --- | --- | --- |
| Round | GET | `/api/rounds/current` | 최신 회차 조회 |
| Round | GET | `/api/rounds/recent` | 최근 회차 목록 (기본 5개) |
| Round | PUT | `/api/rounds/duration/open` | OPEN 구간 길이 변경 |
| Round | PUT | `/api/rounds/duration/closed` | CLOSED 구간 길이 변경 |
| Ticket | POST | `/api/tickets` | 티켓 발행 |
| Ticket | GET | `/api/tickets/{ticketNumber}` | 티켓 상세 조회 |
| Bot | POST | `/api/bots` | 봇 생성 및 POS 자동 할당 |
| Bot | GET | `/api/bots/{botUid}` | 봇 단건 조회 |
| Bot | PUT | `/api/bots/{botUid}/config` | 구매 간격/티켓 수 조정 |
| Bot | PUT | `/api/bots/{botUid}/deactivate` | 비활성화 처리 |
| POS | POST | `/api/pos` | POS 생성 |
| POS | PUT | `/api/pos/{posUid}/status` | 활성/비활성 전환 |
| POS | GET | `/api/pos/{posUid}` | 단건 조회 |
| SSE | GET | `/api/sse/rounds` | 회차 통계 스트리밍 |

## 데이터베이스

PostgreSQL 16을 사용하며 5개 테이블로 구성됩니다.

- **Round**: 회차 번호, 상태(OPEN/CLOSED), 시작/종료 시간
- **Ticket**: 회차 ID, POS UID, 번호 문자열, 타입, 당첨 정보
- **WinningNumber**: 회차별 당첨 번호 6개와 보너스 번호
- **Bot**: 봇 UID, 할당된 POS 목록, 구매 설정, 활성 여부
- **Pos**: POS UID, 판매/당첨 누계, 활성 여부

스키마는 `spring.jpa.hibernate.ddl-auto` 설정으로 관리하며, 환경변수로 create/update/validate를 선택할 수 있습니다.

## 환경 설정

`.env` 파일에서 다음 값을 주입합니다.

```env
# 회차 관리
LOTTO_ROUND_OPEN_DURATION=300
LOTTO_ROUND_CLOSED_DURATION=60
LOTTO_ROUND_CHECK_INTERVAL=10

# 데이터베이스
DB_HOST=localhost
DB_PORT=5432
DB_NAME=lotto
DB_USER=lotto_user
DB_PASSWORD=lotto_pass

# 봇 설정
BOT_DEFAULT_POS_COUNT=3

# SSE
SSE_UPDATE_INTERVAL=5000
```

## 한계 및 미구현 사항

추첨 로직에 `@Async`가 선언되어 있지만 `@EnableAsync`가 없어 실제로는 동기로 실행됩니다. POS 터미널이 전송하는 통계 API(`/api/pos-terminals/statistics`)는 아직 구현되지 않아 호출이 실패합니다. 관리자용 수동 추첨 API, 다중 티켓 구매, 상세 통계 집계 기능은 문서에만 존재합니다.

## 실행

```bash
# 로컬 실행
./gradlew :lotto-central-server:bootRun

# Docker Compose
docker-compose up -d central-server
```

PostgreSQL이 먼저 실행되어 있어야 하며, 포트 8100에서 API를 제공합니다.
