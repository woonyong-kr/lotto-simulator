package org.woonyong.lotto.pos.terminal.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.woonyong.lotto.pos.terminal.domain.TerminalId;

@Component
public class TerminalInitializer {
  private static final String REGISTER_MESSAGE = "터미널 등록: ";
  private static final String REGISTER_SUCCESS_MESSAGE = "터미널 등록 성공: ";
  private static final String REGISTER_FAILURE_MESSAGE = "터미널 등록 실패: ";

  private final TerminalId terminalId;
  private final PosManagerClient posManagerClient;

  public TerminalInitializer(final PosManagerClient posManagerClient) {
    this.terminalId = TerminalId.generate();
    this.posManagerClient = posManagerClient;
  }

  @PostConstruct
  public void initialize() {
    registerToManager();
  }

  public String getTerminalId() {
    return terminalId.getValue();
  }

  private void registerToManager() {
    try {
      System.out.println(REGISTER_MESSAGE + terminalId.getValue());
      posManagerClient.registerTerminal(terminalId.getValue());
      System.out.println(REGISTER_SUCCESS_MESSAGE + terminalId.getValue());
    } catch (Exception e) {
      System.err.println(REGISTER_FAILURE_MESSAGE + terminalId.getValue() + " - " + e.getMessage());
    }
  }
}
