package com.example.agent.mcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Component
public class McpHttpClient {

    private final RestClient restClient;

    public McpHttpClient(RestClient.Builder builder,
                         @Value("${mcp.server-url}") String serverUrl) {
        this.restClient = builder
                .baseUrl(serverUrl)
                .build();
    }

    public String call(String prompt) {
        McpRequest request = new McpRequest(
                UUID.randomUUID().toString(),
                "2.0",
                "chat",
                Map.of("prompt", prompt)
        );

        McpResponse response = restClient.post()
                .body(request)
                .retrieve()
                .body(McpResponse.class);

        return response != null ? response.result() : null;
    }
}

