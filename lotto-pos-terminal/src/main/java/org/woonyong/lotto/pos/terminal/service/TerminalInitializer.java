package org.woonyong.lotto.pos.terminal.service;

import org.springframework.stereotype.Component;
import org.woonyong.lotto.pos.terminal.domain.TerminalId;

@Component
public class TerminalInitializer {
  private static final String REGISTER_MESSAGE = "터미널 등록: ";

  private final TerminalId terminalId;

  public TerminalInitializer() {
    this.terminalId = TerminalId.generate();
  }

  public void initialize() {
    registerToManager();
  }

  public String getTerminalId() {
    return terminalId.getValue();
  }

  private void registerToManager() {
    System.out.println(REGISTER_MESSAGE + terminalId.getValue());
  }
}
