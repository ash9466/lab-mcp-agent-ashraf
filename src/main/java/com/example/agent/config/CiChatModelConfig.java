package com.example.agent.config;

import com.example.agent.agent.BacklogAgent;
import com.example.agent.tools.McpTool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.DisabledChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
@Profile("ci")
public class CiChatModelConfig {

    @Bean
    public ChatModel chatModelStub() {
        return new DisabledChatModel();
    }

    @Bean
    public BacklogAgent backlogAgent(ChatModel chatModelStub, McpTool mcpTool) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(BacklogAgent.class)
                .chatModel(chatModelStub)
                .chatMemory(chatMemory)
                .tools(Collections.singletonList(mcpTool))
                .build();
    }
}

