package org.woonyong.lotto.pos.terminal.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.pos.terminal.config.PosTerminalConfig;

@Component
public class HealthCheckScheduler {
    private static final String LOG_OWNER_VALIDATION_SUCCESS = "오너 유효성 검증 성공";
    private static final String LOG_OWNER_VALIDATION_FAILED = "오너 유효성 검증 실패: {}회";
    private static final String LOG_OWNER_CLEARED = "오너 정보 삭제 및 POS 풀 복귀 요청";
    private static final String LOG_NO_OWNER_REGISTERING = "오너 없음 - POS 매니저에 풀 등록 요청";
    private static final String ERROR_POS_MANAGER_REGISTRATION_FAILED = "POS 매니저 등록 실패: ";

    private final OwnerManager ownerManager;
    private final OwnerValidationClient ownerValidationClient;
    private final PosManagerClient posManagerClient;
    private final TerminalInitializer terminalInitializer;
    private final PosTerminalConfig config;

    public HealthCheckScheduler(final OwnerManager ownerManager,
                              final OwnerValidationClient ownerValidationClient,
                              final PosManagerClient posManagerClient,
                              final TerminalInitializer terminalInitializer,
                              final PosTerminalConfig config) {
        this.ownerManager = ownerManager;
        this.ownerValidationClient = ownerValidationClient;
        this.posManagerClient = posManagerClient;
        this.terminalInitializer = terminalInitializer;
        this.config = config;
    }

    @Scheduled(fixedDelayString = "#{posTerminalConfig.healthCheckIntervalMs}")
    public void performHealthCheck() {
        if (ownerManager.hasOwner()) {
            checkOwnerValidity();
        } else {
            registerToPool();
        }
    }

    private void checkOwnerValidity() {
        try {
            boolean isValid = ownerValidationClient.checkOwnerValidity(
                    ownerManager.getBotClientUrl(),
                    ownerManager.getOwnerBotUid(),
                    ownerManager.getPosUid(),
                    terminalInitializer.getTerminalId()
            );

            if (isValid) {
                ownerManager.resetFailures();
                System.out.println(LOG_OWNER_VALIDATION_SUCCESS);
            } else {
                handleValidationFailure();
            }

        } catch (Exception e) {
            handleValidationFailure();
        }
    }

    private void handleValidationFailure() {
        ownerManager.incrementFailure();
        int failures = ownerManager.getConsecutiveFailures();

        System.out.printf(LOG_OWNER_VALIDATION_FAILED + "%n", failures);

        if (failures >= config.getOwnerValidationFailureThreshold()) {
            clearOwnerAndReturnToPool();
        }
    }

    private void clearOwnerAndReturnToPool() {
        ownerManager.clearOwner();
        System.out.println(LOG_OWNER_CLEARED);
        registerToPool();
    }

    private void registerToPool() {
        try {
            posManagerClient.registerTerminal(terminalInitializer.getTerminalId());
            System.out.println(LOG_NO_OWNER_REGISTERING);
        } catch (Exception e) {
            System.err.println(ERROR_POS_MANAGER_REGISTRATION_FAILED + e.getMessage());
        }
    }
}