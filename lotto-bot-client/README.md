# lotto-bot-client

실제 매장에서 대량 구매를 자동화하는 봇 시스템입니다. 중앙 서버에서 봇 정보를 받아와 인메모리 인스턴스로 관리하고, POS 매니저를 통해 터미널을 할당받아 연결 상태를 유지합니다. 자동 구매 워커는 아직 구현되지
않았지만, 봇 생성과 POS 할당 같은 인프라 레벨 작업은 동작합니다.

## 현재 동작

- **봇 초기화**: 애플리케이션 시작 시 중앙 서버 `/api/bots?active=true`를 호출하여 활성 봇 목록을 가져오고, `BotInstanceManager`에 저장합니다.
  `bot.max-capacity`를 초과하는 봇은 중앙 서버에 비활성화 요청을 보냅니다.
- **봇 생성/삭제 API**: `/api/bots`(POST/DELETE), `/api/bots/{botUid}`(DELETE), `/api/bots/{botUid}/config`(PUT) 등을 통해 중앙 서버와
  동기화된 상태를 유지합니다.
- **POS 할당**: 봇 생성 또는 초기화 시 각 POS UID에 대해 `PosManagerClient.allocateTerminal()`을 호출하고, 성공 시 `BotInstance`에
  `PosTerminalConnection`을 추가합니다. 실패 시 `PosManagerClient` 재시도 대신 로그를 남기고 중앙 서버에 해당 POS 비활성화를 요청합니다.
- **SSE·대시보드 보고**: `/api/stream/bots`로 인메모리 상태를 스트리밍하며, `BotStatusReportScheduler`가 대시보드 URL로 상태 보고를 전송합니다. (Dashboard
  모듈이 (미구현)이므로 실패 로그만 출력)
- **Owner Health API**: POS 터미널이 `/api/health/owner`에 봇 UID/터미널 정보를 보내면 해당 봇 인스턴스가 활성인지 여부를 응답합니다.

## (미구현) 항목

- 자동 티켓 구매 워커 및 전략 실행
- POS 할당 실패 시 무한 재시도·대기 정책
- POS 터미널 해제(`/api/terminals/release`) 호출
- 봇별 성능/구매 통계 수집

## 주요 설정 (`application.yml`)

```yaml
bot:
  max-capacity: ${BOT_MAX_CAPACITY}
  default-purchase-interval-ms: ${BOT_DEFAULT_PURCHASE_INTERVAL_MS}
  default-tickets-per-purchase: ${BOT_DEFAULT_TICKETS_PER_PURCHASE}
  default-pos-count: ${BOT_DEFAULT_POS_COUNT}
  pos-failure-threshold: ${BOT_POS_FAILURE_THRESHOLD}
  pos-reallocation-retry-ms: ${BOT_POS_REALLOCATION_RETRY_MS}
  pos-allocation-max-retry: ${BOT_POS_ALLOCATION_MAX_RETRY}
  central-server-url: ${CENTRAL_SERVER_URL}
  pos-manager-url: ${POS_MANAGER_URL}
  dashboard-server-url: ${DASHBOARD_URL}
  bot-client-url: ${BOT_CLIENT_URL}
```

## API

| 메서드    | 경로                          | 설명                          |
|--------|-----------------------------|-----------------------------|
| POST   | `/api/bots`                 | 봇 생성 후 중앙 서버/Bot Client 동기화 |
| PUT    | `/api/bots/{botUid}/config` | 구매 간격과 티켓 수 변경 (메모리에서만 유지)  |
| DELETE | `/api/bots/{botUid}`        | 봇 비활성화 후 제거                 |
| DELETE | `/api/bots`                 | 모든 봇 삭제                     |
| GET    | `/api/stream/bots`          | SSE로 현재 봇/터미널 상태 스트림        |
| POST   | `/api/health/owner`         | POS 터미널 소유권 검증              |
| GET    | `/api/bot/health`           | 헬스 체크                       |

## 실행

루트에서 `./gradlew :lotto-bot-client:bootRun` 또는 Docker Compose로 실행합니다. 중앙 서버와 POS 매니저가 먼저 떠 있어야 API 호출이 성공합니다.
