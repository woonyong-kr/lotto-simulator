package org.woonyong.lotto.bot.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.woonyong.lotto.bot.config.BotClientConfig;
import org.woonyong.lotto.bot.dto.request.AllocateTerminalRequest;
import org.woonyong.lotto.bot.dto.response.AllocateTerminalResponse;

@Component
public class PosManagerClient {
  private static final String TERMINALS_ALLOCATE_ENDPOINT = "/api/terminals/allocate";
  private static final String TERMINALS_RELEASE_ENDPOINT = "/api/terminals/release/";

  private final RestTemplate restTemplate;
  private final String posManagerUrl;

  public PosManagerClient(final RestTemplate restTemplate, final BotClientConfig botClientConfig) {
    this.restTemplate = restTemplate;
    this.posManagerUrl = botClientConfig.getPosManagerUrl();
  }

  public AllocateTerminalResponse allocateTerminal(final String botUid, final String posUid) {
    String url = posManagerUrl + TERMINALS_ALLOCATE_ENDPOINT;
    AllocateTerminalRequest request = new AllocateTerminalRequest(botUid, posUid);
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<AllocateTerminalRequest> entity = new HttpEntity<>(request, headers);
      return restTemplate.postForObject(url, entity, AllocateTerminalResponse.class);
    } catch (Exception e) {
      return null;
    }
  }

  public boolean releaseTerminal(final String posUid) {
    String url = posManagerUrl + TERMINALS_RELEASE_ENDPOINT + posUid;
    try {
      restTemplate.delete(url);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
