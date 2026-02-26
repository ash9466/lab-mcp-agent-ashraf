package com.example.agent.mcp;

import java.util.Map;

public record McpRequest(
        String id,
        String jsonrpc,
        String method,
        Map<String, Object> params
) {
}

