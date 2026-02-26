package com.example.agent.config;

import com.example.agent.agent.BacklogAgent;
import com.example.agent.tools.McpTool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.Collections;

@Configuration
@Profile("!ci")
public class AnthropicConfig {

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${anthropic.model-name}")
    private String modelName;

    @Bean
    public ChatModel anthropicChatModel() {
        return AnthropicChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    @Bean
    public BacklogAgent backlogAgent(ChatModel anthropicChatModel, McpTool mcpTool) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(BacklogAgent.class)
                .chatModel(anthropicChatModel)
                .chatMemory(chatMemory)
                .tools(Collections.singletonList(mcpTool))
                .build();
    }
}



