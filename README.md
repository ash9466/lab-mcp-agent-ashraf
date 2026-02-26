# AI MCP Agent – Spring Boot 3

Projet de démonstration d’un **agent IA connecté à un serveur MCP**, basé sur **Spring Boot 3.5**, **Java 21**, **LangChain4j 1.10.0** et **Anthropic Claude**.

Ce projet a été réalisé par **Ashraf ADEL SABAA**.

---

## 1. Architecture générale

- **Backend**: Spring Boot (Gradle, Java 21)
- **API REST**:
  - `POST /api/users` / `GET /api/users/{id}` (slice utilisateur simple)
  - `POST /api/chat` (conversation avec l’agent IA)
- **IA & LangChain4j**:
  - `BacklogAgent` (interface annotée `@SystemMessage`) pour jouer le rôle de Product Owner.
  - Intégration Anthropic via `AnthropicChatModel` (profil normal).
  - Profil `ci` utilisant `DisabledChatModel` pour ne pas nécessiter de vraie clé API en CI.
  - Mémoire de conversation via `MessageWindowChatMemory`.
- **MCP**:
  - Client HTTP JSON‑RPC (`McpHttpClient`) vers `mcp.server-url`.
  - Outil LangChain4j (`McpTool`) exposé à l’agent pour générer/analyser du backlog.

---

## 2. Prérequis

- **Java 21**
- **Docker** (optionnel, pour builder l’image)
- (Optionnel) **Minikube** si tu veux utiliser le job Kubernetes de la CI localement.

---

## 3. Configuration

### 3.1. Variables Anthropic & MCP

La configuration principale se trouve dans `src/main/resources/application.yml` :

```yaml
anthropic:
  api-key: ${ANTHROPIC_API_KEY:missing-key}
  model-name: claude-3-5-sonnet-20241022

mcp:
  server-url: http://localhost:8081/mcp/chat
```

- Définit la variable d’environnement `ANTHROPIC_API_KEY` pour utiliser un vrai modèle.
- Le profil `ci` utilise un `DisabledChatModel`, donc aucune clé n’est requise en CI.

---

## 4. Lancer l’application localement

Depuis la racine du projet :

```bash
./gradlew bootRun
```

Endpoints utiles :

- **User slice**
  - Création d’un utilisateur :
    ```bash
    curl -X POST "http://localhost:8080/api/users?name=Alice&email=alice@example.com"
    ```
  - Récupération par id :
    ```bash
    curl "http://localhost:8080/api/users/<UUID>"
    ```

- **Chat avec l’agent**
  ```bash
  curl -X POST "http://localhost:8080/api/chat" \
    -H "Content-Type: application/json" \
    -d '{"message": "Peux-tu m’aider à prioriser ce backlog ?"}'
  ```

---

## 5. Build, tests et CI

### 5.1. Build & tests locaux

```bash
./gradlew build
```

Ce build exécute :
- les tests unitaires et d’intégration (`UserControllerIT`, `ChatControllerIT`),
- le packaging `bootJar`.

### 5.2. GitHub Actions

Le workflow CI se trouve dans `.github/workflows/ci.yml` et :
- compile & teste le projet avec Java 21,
- construit une image Docker dans Minikube (`ai-agent:ci`),
- déploie le `Deployment` + `Service` Kubernetes (manifeste dans `src/main/k8s/ai-agent-deployment.yml`),
- vérifie `/actuator/health`.

---

## 6. Docker & Kubernetes

### 6.1. Image Docker locale

```bash
./gradlew bootJar
docker build -t ai-agent:ci .
```

### 6.2. Déploiement (exemple Minikube)

```bash
eval $(minikube -p minikube docker-env)
docker build -t ai-agent:ci .
kubectl apply -f src/main/k8s/ai-agent-deployment.yml
kubectl rollout status deployment/ai-agent --timeout=180s
```

---

## 7. Profils Spring

- **Profil par défaut** (prod/dev) :
  - Utilise Anthrop ic (`AnthropicConfig`) avec `BacklogAgent` branché sur Claude + MCP.
- **Profil `ci`** :
  - Utilise `CiChatModelConfig` avec `DisabledChatModel` (pas d’appel externe),
  - garde la même API (`/api/chat`) mais renvoie éventuellement une réponse vide,
  - permet des tests reproductibles sans secrets.

---

## 8. Auteur

Projet développé avec l’aide de l’IA, et porté par **Ashraf ADEL SABAA**.  
N’hésite pas à adapter ce squelette pour tes propres agents IA et serveurs MCP.

