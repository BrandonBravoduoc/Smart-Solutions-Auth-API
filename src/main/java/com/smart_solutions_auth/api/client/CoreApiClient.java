package com.smart_solutions_auth.api.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CoreApiClient {

    private static final Logger log = LoggerFactory.getLogger(CoreApiClient.class);

    private final RestClient restClient;

    public CoreApiClient(@Value("${core.api.base-url}") String coreApiBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(coreApiBaseUrl).build();
    }

    public boolean hasActiveSubscription(Long userId) {
        try {
            Map<String, Object> body = restClient.get()
                    .uri("/api/v1/subscriptions/{userId}/subscription", userId)
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Role", "ADMINISTRADOR")
                    .retrieve()
                    .body(Map.class);

            return body != null && "ACTIVE".equals(body.get("status"));
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            log.warn("No se pudo verificar la suscripción del usuario {} en Core API: {}", userId, e.getMessage());
            return false;
        }
    }
}
