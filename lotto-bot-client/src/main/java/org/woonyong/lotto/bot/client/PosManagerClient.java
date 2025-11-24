package org.woonyong.lotto.bot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.woonyong.lotto.bot.dto.request.AllocateTerminalRequest;
import org.woonyong.lotto.bot.dto.response.AllocateTerminalResponse;

@Component
public class PosManagerClient {
    private static final Logger log = LoggerFactory.getLogger(PosManagerClient.class);

    private final RestTemplate restTemplate;
    private final String posManagerUrl;

    public PosManagerClient(
            final RestTemplate restTemplate,
            @Value("${pos.manager.url}") final String posManagerUrl
    ) {
        this.restTemplate = restTemplate;
        this.posManagerUrl = posManagerUrl;
    }

    public AllocateTerminalResponse allocateTerminal(
            final String botUid,
            final String posUid
    ) {
        String url = posManagerUrl + "/api/terminals/allocate";
        AllocateTerminalRequest request = new AllocateTerminalRequest(botUid, posUid);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AllocateTerminalRequest> entity = new HttpEntity<>(request, headers);
            return restTemplate.postForObject(url, entity, AllocateTerminalResponse.class);
        } catch (Exception e) {
            log.error("터미널 할당 실패: botUid={}, posUid={}, error={}",
                    botUid, posUid, e.getMessage());
            return null;
        }
    }

    public boolean releaseTerminal(final String posUid) {
        String url = posManagerUrl + "/api/terminals/release/" + posUid;
        try {
            restTemplate.delete(url);
            return true;
        } catch (Exception e) {
            log.error("터미널 해제 실패: posUid={}, error={}", posUid, e.getMessage());
            return false;
        }
    }
}
