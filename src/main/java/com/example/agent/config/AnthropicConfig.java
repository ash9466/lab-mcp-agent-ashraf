package com.example.agent.config;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

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
}

