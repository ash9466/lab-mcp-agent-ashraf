package com.example.agent.agent;

import dev.langchain4j.service.SystemMessage;

@SystemMessage("Tu es un Product Owner expert. Ton rôle est d'analyser les demandes et d'aider à la gestion du backlog.")
public interface BacklogAgent {

    String chat(String userMessage);
}

