package com.example.agent.mcp;

public record McpResponse(
        String id,
        String jsonrpc,
        String result
) {
}

