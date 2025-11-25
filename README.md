# Lotto Simulator

한국 로또 6/45 시스템을 분산 아키텍처로 구현한 시뮬레이터입니다.

## 프로젝트 소개

실제 로또 서비스는 중앙 서버, 판매점, POS 단말기, 자동 구매 시스템이 각각 독립된 프로세스로 동작합니다. 이 프로젝트는 이런 분산 환경에서 발생하는 네트워크 지연, 동시성 제어, 장애 처리 같은 복잡한 문제들을 직접 다뤄보기 위해 만들었습니다.

각 컴포넌트를 별도의 Spring Boot 애플리케이션으로 구현하고 Docker 컨테이너로 배포했습니다. 회차와 티켓을 관리하는 중앙 서버, 구매를 처리하는 POS 터미널, 터미널을 관리하는 POS 매니저, 대량 구매를 시뮬레이션하는 봇 클라이언트가 서로 REST API로 통신하면서 실제 서비스와 유사한 환경을 만들어냅니다.

## 주요 특징

- **독립 프로세스 구조**: 6개 모듈이 각각 독립된 Spring Boot 애플리케이션으로 동작
- **컨테이너 기반 배포**: Docker Compose로 전체 시스템을 한 번에 실행
- **실시간 통신**: REST API와 Server-Sent Events(SSE)를 활용한 실시간 데이터 스트리밍
- **동시성 제어**: PostgreSQL Pessimistic Lock을 통한 안전한 티켓 발행
- **회차 자동화**: 스케줄러 기반 회차 생성, 마감, 추첨 자동 처리

## 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.4.0
- **데이터베이스**: PostgreSQL 16
- **빌드 도구**: Gradle (Kotlin DSL)
- **컨테이너**: Docker, Docker Compose

## 시스템 아키텍처

```
┌───────────────┐        ┌────────────────┐
│   Bot Client  │ <────> │ POS Manager    │
└──────┬────────┘        └──────┬─────────┘
       │ POS 할당/해제                │ 터미널 풀 관리
┌──────▼────────┐        ┌──────▼────────┐
│  Central      │ <────> │ POS Terminal │
│  Server       │  티켓  │ (다중 인스턴스)
└───────────────┘        └──────────────┘
            ▲                      │
            │ 회차/티켓 API         │ 구매 위임
            ▼                      ▼
        PostgreSQL            Dashboard
```

## 빠른 시작

```bash
# 1. 전체 빌드 및 실행
./gradlew :lotto-core:build
./gradlew build -x test
docker-compose up -d

# 2. 헬스 체크
curl http://localhost:8100/api/central/health
curl http://localhost:8200/api/bot/health
curl http://localhost:8300/api/pos-manager/health
curl http://localhost:8400/api/pos-terminal/health
```

## 모듈 구성

| 모듈 | 포트 | 설명 |
|------|------|------|
| **lotto-core** | - | 공통 도메인 모델, DTO, 유틸리티 |
| **lotto-central-server** | 8100 | 회차 관리, 티켓 발행, 당첨 처리 |
| **lotto-pos-manager** | 8300 | POS 터미널 풀 관리 및 할당 |
| **lotto-pos-terminal** | 8400+ | 구매 요청 처리 및 중앙 서버 연동 |
| **lotto-bot-client** | 8200 | 자동 구매 봇 관리 |
| **lotto-dashboard** | 8500 | 모니터링 대시보드 (미완성) |

각 모듈의 상세 정보는 해당 디렉터리의 README를 참고하세요.

## 구현 현황

### ✅ 구현 완료
- 회차 자동 생성, 마감, 추첨
- 티켓 발행 및 동시성 제어
- 봇/POS 생성 및 관리 API
- POS 터미널 풀 관리
- SSE를 통한 실시간 회차 정보 스트리밍

### ⏳ 부분 구현
- POS 터미널 헬스체크 (HTTP만 지원, Docker 연동 미구현)
- 봇 클라이언트 인프라 (구매 워커 미구현)
- 대시보드 골격 (UI 및 연동 로직 미구현)

### ❌ 미구현
- 자동 구매 워커 및 전략
- POS 통계 수집 API
- Docker 기반 터미널 자동 복구
- 다중 티켓 구매
- 관리자용 수동 추첨 기능

## 문서

- **기술 문서**: `docs/` 디렉터리에 아키텍처, API 명세, 배포 가이드 포함
- **개발 기록**: `task-docs/` 디렉터리에 설계 과정 문서 보관
- **모듈별 가이드**: 각 모듈 디렉터리의 `README.md` 참고

> **주의**: 일부 문서는 기획 단계에서 작성되어 실제 구현과 다를 수 있습니다. 실제 동작은 이 README와 각 모듈 README를 기준으로 확인하세요.
