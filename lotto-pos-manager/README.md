# lotto-pos-manager

실제 판매점에서는 여러 대의 POS 단말기를 관리해야 합니다. 중앙 서버가 개별 단말기를 직접 다루면 복잡도가 높아지기 때문에, 터미널 풀을 전담하는 관리 서버를 별도로 만들었습니다. 봇이 구매용 POS를 요청하면
가용한 터미널을 할당해주고, 사용이 끝나면 다시 풀로 회수하는 역할을 합니다.

## 구현된 기능

- **터미널 등록**: POS 터미널이 `/api/terminals/register`에 POST 하면 `TerminalPoolManager`가 `terminalId`와 주소를 기록하고 가용 풀에 추가합니다.
- **터미널 할당**: 봇 클라이언트가 `/api/terminals/allocate`에 봇 UID·POS UID·봇 클라이언트 URL을 전달하면 가용 풀에서 하나를 pop하여 반환하고, POS 터미널에 오너 정보를
  전달합니다.
- **터미널 해제**: `/api/terminals/release`로 다시 풀에 되돌립니다.
- **SSE 모니터링**: `/api/stream/pos-status`에서 전체 용량 대비 인스턴스 수와 가용 수를 주기적으로 전송합니다.
- **헬스체크 로그**: `TerminalHealthChecker`가 주기적으로 등록된 터미널 주소의 `/health` 엔드포인트를 호출하고 결과를 콘솔에 출력합니다.

## 한계 및 (미구현) 항목

- 문서에 있던 Docker 컨테이너 상태 조회·자동 재생성 로직은 (미구현)입니다. 현재는 WebClient로 HTTP 헬스만 확인합니다.
- POS 터미널 오너 정보는 `TerminalInfo`에 저장되지 않습니다. 단순히 ID/주소/상태만 추적합니다.
- 풀 데이터는 메모리에만 존재하므로 애플리케이션을 재시작하면 모든 등록 정보가 사라집니다. POS 터미널이 재등록하여 복구해야 합니다.

## API 요약

| 메서드  | 경로                        | 설명                                       |
|------|---------------------------|------------------------------------------|
| POST | `/api/terminals/register` | `{ "terminalId": "TERM-XXXX" }`          |
| POST | `/api/terminals/allocate` | `{ "botUid", "posUid", "botClientUrl" }` |
| POST | `/api/terminals/release`  | `{ "terminalId", "botUid", "posUid" }`   |
| GET  | `/api/stream/pos-status`  | SSE 스트림                                  |
| GET  | `/api/pos-manager/health` | 단순 문자열 헬스체크                              |

## 환경 변수

- `POS_MANAGER_MAX_TERMINAL_CAPACITY`: UI/문서용 총 용량 값
- `POS_MANAGER_HEALTH_CHECK_INTERVAL_MS`: 헬스체크 주기
- `POS_MANAGER_READ_TIMEOUT`: POS 터미널 호출 타임아웃
- `SSE_UPDATE_INTERVAL`: SSE 주기
- `CENTRAL_SERVER_URL`, `DASHBOARD_URL`: 현재 서비스에서는 사용하지 않습니다. (미구현)

## 실행

루트에서 `./gradlew :lotto-pos-manager:bootRun` 또는 Docker Compose로 실행하면 8300 포트에서 API를 확인할 수 있습니다.
