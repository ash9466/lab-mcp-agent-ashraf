package com.example.agent.config;

import com.example.agent.mcp.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("ci")
public class CiChatModelConfig {

    @Bean
    public ChatModel chatModelStub() {
        return prompt -> "stub-response";
    }
}

