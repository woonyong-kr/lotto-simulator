package org.woonyong.lotto.pos.manager.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.pos.manager.domain.TerminalInfo;

@Component
public class TerminalHealthChecker {
    private static final String LOG_HEALTH_CHECK_START = "터미널 헬스체크 시작: {}";
    private static final String LOG_TERMINAL_HEALTHY = "터미널 정상: {}";
    private static final String LOG_TERMINAL_UNHEALTHY = "터미널 비정상 감지: {}";
    private static final String LOG_CONTAINER_RECREATION_STARTED = "컨테이너 재생성 시작: {}";
    private static final String LOG_CONTAINER_RECREATION_COMPLETED = "컨테이너 재생성 완료: {}";
    private static final String ERROR_CONTAINER_RECREATION_FAILED = "컨테이너 재생성 실패: {}";

    private final TerminalPoolManager terminalPoolManager;
    private final PosTerminalClient posTerminalClient;

    public TerminalHealthChecker(final TerminalPoolManager terminalPoolManager,
                                 final PosTerminalClient posTerminalClient) {
        this.terminalPoolManager = terminalPoolManager;
        this.posTerminalClient = posTerminalClient;
    }

    @Scheduled(fixedDelayString = "#{posManagerConfig.healthCheckIntervalMs}")
    public void checkAllTerminals() {
        System.out.println("=== 터미널 헬스체크 시작 ===");

        for (TerminalInfo terminal : terminalPoolManager.getAllTerminals()) {
            checkTerminal(terminal);
        }

        System.out.println("=== 터미널 헬스체크 완료 ===");
    }

    private void checkTerminal(final TerminalInfo terminal) {
        System.out.printf(LOG_HEALTH_CHECK_START + "%n", terminal.getTerminalId());

        try {
            boolean isHealthy = posTerminalClient.checkHealth(terminal.getAddress());

            if (isHealthy) {
                terminal.markHealthy();
                System.out.printf(LOG_TERMINAL_HEALTHY + "%n", terminal.getTerminalId());
            } else {
                handleUnhealthyTerminal(terminal);
            }

        } catch (Exception e) {
            handleUnhealthyTerminal(terminal);
        }
    }

    private void handleUnhealthyTerminal(final TerminalInfo terminal) {
        terminal.markUnhealthy();
        System.out.printf(LOG_TERMINAL_UNHEALTHY + "%n", terminal.getTerminalId());

        // 풀에서 제거
        terminalPoolManager.remove(terminal.getTerminalId());

        // 컨테이너 재생성
        recreateContainer(terminal);
    }

    private void recreateContainer(final TerminalInfo terminal) {
        System.out.printf(LOG_CONTAINER_RECREATION_STARTED + "%n", terminal.getTerminalId());

        try {
            // TODO: Docker 컨테이너 재생성 로직 구현
            // 1. 기존 컨테이너 중지 및 삭제
            // dockerClient.stopContainer(terminal.getContainerId());
            // dockerClient.removeContainer(terminal.getContainerId());

            // 2. 새 컨테이너 생성
            // String newContainerId = dockerClient.createContainer("lotto-pos-terminal", port);
            // dockerClient.startContainer(newContainerId);

            // 3. 터미널 정보 업데이트
            // terminal.updateContainerId(newContainerId);
            // terminal.markHealthy();

            // 4. 풀에 다시 등록
            // terminalPoolManager.register(terminal.getTerminalId(), terminal.getAddress(), newContainerId);

            System.out.printf(LOG_CONTAINER_RECREATION_COMPLETED + "%n", terminal.getTerminalId());

        } catch (Exception e) {
            System.err.printf(ERROR_CONTAINER_RECREATION_FAILED + "%n", e.getMessage());
        }
    }
}