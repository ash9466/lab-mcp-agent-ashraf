package com.example.agent.tools;

import com.example.agent.mcp.McpHttpClient;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class McpTool {

    private final McpHttpClient mcpHttpClient;

    public McpTool(McpHttpClient mcpHttpClient) {
        this.mcpHttpClient = mcpHttpClient;
    }

    @Tool("Outil pour générer ou analyser des éléments de backlog en utilisant le serveur MCP.")
    public String generateBacklog(String prompt) {
        return mcpHttpClient.call(prompt);
    }
}

