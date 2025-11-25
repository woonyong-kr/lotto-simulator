# lotto-pos-terminal

실제 판매점에 있는 POS 단말기처럼 독립 프로세스로 동작하는 터미널입니다. 각 터미널은 별도 컨테이너로 실행되며, 봇이나 판매점에서 오는 구매 요청을 받아 중앙 서버에 전달합니다. 응답 시간을 측정하고, 오너를
검증하며, 장애 시 자동으로 재등록을 시도합니다.

## 제공 기능

- `/api/owner` (POST): POS 매니저가 봇 UID·POS UID·봇 클라이언트 URL을 전달하면 `OwnerManager`가 메모리에 저장합니다. 이미 오너가 있을 때는 409 에러를 반환합니다.
- `/api/tickets/purchase` (POST): 오너가 설정된 경우에만 요청을 허용하며, `CentralServerClient`를 통해 중앙 서버 `/api/tickets`를 호출합니다. 응답 시간을
  측정해 `ResponseTimeCollector`에 기록합니다.
- `/api/pos-terminal/health`: 헬스체크
- 헬스 체크 스케줄러: 주기적으로 봇 클라이언트에 오너 검증을 요청하고, 실패 횟수가 임계값을 넘으면 오너를 해제하고 POS 매니저에 다시 등록을 시도합니다.
- 통계 보고 스케줄러: 수집된 응답 시간의 평균을 계산해 중앙 서버 `/api/pos-terminals/statistics`에 전송 시도(엔드포인트가 (미구현)이므로 현재는 실패 예외가 발생).

## 한계

- README에 있었던 “슬라이딩 윈도우 최근 100개” 로직은 (미구현)입니다. 현재는 모든 응답 시간을 리스트에 누적했다가 평균을 낸 뒤 전체 삭제합니다.
- POS 매니저 재시작 시 자동으로 풀에 다시 등록하기 위해 주기적으로 `/api/terminals/register`를 호출하지만, 실패 시 즉시 재시도하지 않고 로그만 남깁니다.
- 중앙 서버와 POS 매니저 의존 API가 실패할 경우 무한 재시도 로직이 없으며, 대부분 예외를 그대로 던집니다.

## 환경 변수

- `POS_TERMINAL_PORT`
- `CENTRAL_SERVER_URL`, `POS_MANAGER_URL`
- `STATISTICS_REPORT_INTERVAL_MS`, `HEALTH_CHECK_INTERVAL_MS`, `OWNER_VALIDATION_FAILURE_THRESHOLD`
- `CONNECTION_TIMEOUT_MS`, `READ_TIMEOUT_MS`

## 실행

루트에서 `./gradlew :lotto-pos-terminal:bootRun` 또는 Docker Compose를 사용해 여러 인스턴스를 띄울 수 있습니다. 각 인스턴스는 시작 시 무작위 `terminalId`를
생성합니다.
