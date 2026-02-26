package com.example.agent.web;

import com.example.agent.agent.BacklogAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final BacklogAgent backlogAgent;

    public ChatController(BacklogAgent backlogAgent) {
        this.backlogAgent = backlogAgent;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String answer = backlogAgent.chat(request.message());
        return new ChatResponse(answer);
    }
}

