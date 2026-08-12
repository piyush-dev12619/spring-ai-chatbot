# 🤖 Spring AI Chat Application

> A Spring Boot application that provides an AI-powered chatbot using
> **Spring AI + Ollama + MongoDB**, with a browser-based chat UI.

------------------------------------------------------------------------

## ✨ Project Overview

This project demonstrates how to build an AI chatbot with Java and
Spring Boot.

### Architecture

``` text
┌─────────────────────┐
│   Browser / Chat UI │
│      index.html     │
└──────────┬──────────┘
           │
           │ POST /api/chat
           ▼
┌─────────────────────┐
│   Spring Boot API   │
│   ChatController    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     Spring AI       │
│      ChatClient     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│       Ollama        │
│   Local LLM Model   │
└──────────┬──────────┘
           │
           ▼
      AI Response

MongoDB
   │
   └── Used for application/chat data persistence
```

------------------------------------------------------------------------

## 🛠️ Technology Stack

  Technology                    Purpose
  ----------------------------- ----------------------------------
  **Java 21**                   Application programming language
  **Spring Boot**               Backend framework
  **Spring AI**                 AI integration
  **Ollama**                    Local LLM runtime
  **MongoDB**                   Data persistence
  **Maven**                     Build and dependency management
  **Lombok**                    Reduces Java boilerplate
  **HTML / CSS / JavaScript**   Chatbot frontend

------------------------------------------------------------------------

## 📋 Prerequisites

Install the following before running the application:

-   Java 21+
-   Maven 3.6+
-   MongoDB or MongoDB Atlas
-   Ollama
-   An Ollama model such as `llama3.2`, `mistral`, or another supported
    chat model

Verify Java:

``` bash
java -version
```

Verify Maven:

``` bash
mvn -version
```

Verify Ollama:

``` bash
ollama --version
```

------------------------------------------------------------------------

# 🚀 Getting Started

## 1. Clone the Project

``` bash
git clone <repository-url>
cd springai
```

------------------------------------------------------------------------

## 2. Configure MongoDB

For a local MongoDB instance:

``` properties
spring.data.mongodb.uri=mongodb://localhost:27017/springai
spring.data.mongodb.database=springai
```

For MongoDB Atlas, use your Atlas connection string:

``` properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>/<database>
spring.data.mongodb.database=springai
```

> ⚠️ **Security:** Never commit database usernames, passwords, API keys,
> or other secrets to GitHub. Prefer environment variables or an
> external configuration file.

------------------------------------------------------------------------

## 3. Configure Ollama

Start Ollama and make sure a model is available.

Example:

``` bash
ollama pull llama3.2
```

Verify installed models:

``` bash
ollama list
```

Ollama normally runs at:

``` text
http://localhost:11434
```

Example configuration:

``` properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=llama3.2
```

> Use the exact model name shown by `ollama list`.

------------------------------------------------------------------------

# ⚙️ Application Configuration

Example `application.properties`:

``` properties
# Server
server.port=8080

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/springai
spring.data.mongodb.database=springai

# Spring AI - Ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=llama3.2
```

If your project uses `application.yml`, the equivalent configuration is:

``` yaml
server:
  port: 8080

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/springai
      database: springai

  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: llama3.2
```

------------------------------------------------------------------------

# ▶️ Running the Application

### Option 1 --- Maven

``` bash
mvn spring-boot:run
```

### Option 2 --- IntelliJ IDEA

1.  Open the project in IntelliJ IDEA.
2.  Make sure Java 21 is configured.
3.  Open `SpringaiApplication.java`.
4.  Click **Run**.
5.  Wait for Spring Boot to start.

The application should be available at:

``` text
http://localhost:8080
```

------------------------------------------------------------------------

# 💬 Chat API

## POST `/api/chat`

Sends a message to the AI model and returns the generated response.

### Request

``` http
POST http://localhost:8080/api/chat
Content-Type: text/plain
```

Request body:

``` text
What is Spring Boot?
```

### Example Controller

``` java
@PostMapping("/chat")
public String chat(@RequestBody String message) {

    return chatClient
            .prompt(message)
            .call()
            .content();
}
```

### Example Response

``` text
Spring Boot is a framework built on top of Spring that simplifies
the development of Java applications...
```

------------------------------------------------------------------------

# 🧪 Testing with Postman

Create a new request:

``` text
POST http://localhost:8080/api/chat
```

Select:

``` text
Body
 → raw
 → Text
```

Enter:

``` text
What is Java?
```

Add this header:

``` text
Content-Type: text/plain
```

Click **Send**.

Expected result:

``` text
Java is a high-level, object-oriented programming language...
```

------------------------------------------------------------------------

# 🌐 Chat UI

The project can serve the frontend directly from Spring Boot.

Place:

``` text
src/main/resources/static/index.html
```

Project structure:

``` text
springai/
└── src/
    └── main/
        ├── java/
        │   └── com/piyush/firstproject/springai/
        │       ├── SpringaiApplication.java
        │       └── ChatController/
        │           └── ChatController.java
        │
        └── resources/
            ├── static/
            │   └── index.html
            │
            └── application.properties
```

Open the UI at:

``` text
http://localhost:8080/index.html
```

The frontend can call the backend using:

``` javascript
const response = await fetch("/api/chat", {
    method: "POST",
    headers: {
        "Content-Type": "text/plain"
    },
    body: message
});
```

### Why use `/api/chat` instead of `http://localhost:8080/api/chat`?

Because the frontend is served by the same Spring Boot application.

``` text
Browser
   │
   ├── /index.html
   │
   └── /api/chat
          │
          ▼
      Spring Boot
```

This avoids the cross-origin issue you can encounter when opening the
HTML through IntelliJ's separate development server such as:

``` text
http://localhost:63342
```

------------------------------------------------------------------------

# 🧠 How the AI Request Works

When the user enters:

``` text
Tell me about Java
```

the flow is:

``` text
User
  │
  ▼
Chat UI
  │
  │ POST /api/chat
  ▼
ChatController
  │
  ▼
ChatClient
  │
  ▼
Spring AI
  │
  ▼
Ollama
  │
  ▼
LLM
  │
  ▼
AI Response
  │
  ▼
Chat UI
```

The key Spring AI code is:

``` java
chatClient
        .prompt(message)
        .call()
        .content();
```

### What each method does

  Method              Purpose
  ------------------- ----------------------------------------------
  `prompt()`          Starts building the AI request
  `prompt(message)`   Adds the user's message
  `call()`            Sends the request to the configured AI model
  `content()`         Gets the generated text response

------------------------------------------------------------------------

# 🗃️ MongoDB

MongoDB is included for persistence.

The current application can use MongoDB for storing entities such as:

``` text
ChatMessage
```

A typical chat document could contain:

``` json
{
  "sessionId": "abc123",
  "role": "user",
  "message": "What is Java?"
}
```

and:

``` json
{
  "sessionId": "abc123",
  "role": "assistant",
  "message": "Java is a high-level programming language..."
}
```

This can later be used to implement **chat history and conversation
memory**.

------------------------------------------------------------------------

# 🧩 Project Structure

``` text
springai/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/piyush/firstproject/springai/
│   │   │       ├── SpringaiApplication.java
│   │   │       │
│   │   │       ├── ChatController/
│   │   │       │   └── ChatController.java
│   │   │       │
│   │   │       └── Entity/
│   │   │           └── ChatResponse.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│       └── SpringaiApplicationTests.java
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── HELP.md
```

------------------------------------------------------------------------

# 🔨 Build Commands

### Clean and build

``` bash
mvn clean install
```

### Run tests

``` bash
mvn test
```

### Create JAR

``` bash
mvn package
```

### Run JAR

``` bash
java -jar target/springai-0.0.1-SNAPSHOT.jar
```

------------------------------------------------------------------------

# 🐛 Troubleshooting

## ❌ Ollama Connection Error

Check whether Ollama is running:

``` bash
ollama list
```

Try running the model manually:

``` bash
ollama run llama3.2
```

Check the configured URL:

``` text
http://localhost:11434
```

------------------------------------------------------------------------

## ❌ Model Not Found

Check installed models:

``` bash
ollama list
```

Pull the required model:

``` bash
ollama pull llama3.2
```

Make sure the configuration uses the same model name:

``` properties
spring.ai.ollama.chat.model=llama3.2
```

------------------------------------------------------------------------

## ❌ MongoDB Connection Error

Check:

-   MongoDB is running.
-   The connection URI is correct.
-   The database server is reachable.
-   MongoDB Atlas network access allows your connection if using Atlas.

------------------------------------------------------------------------

## ❌ CORS Error

If the browser shows:

``` text
No 'Access-Control-Allow-Origin' header
```

check whether the frontend is running from a different origin, for
example:

``` text
Frontend: http://localhost:63342
Backend:  http://localhost:8080
```

Prefer serving the frontend from:

``` text
src/main/resources/static/index.html
```

and opening:

``` text
http://localhost:8080/index.html
```

------------------------------------------------------------------------

## ❌ `ChatClient` Bean Not Found

If you see:

``` text
Field ChatClient required a bean of type
'org.springframework.ai.chat.client.ChatClient'
that could not be found
```

make sure your Spring AI Ollama starter/dependencies are correctly
configured and that your controller uses:

``` java
public ChatController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
}
```

------------------------------------------------------------------------

# 🔐 Security Notes

Never hard-code secrets such as:

``` properties
spring.ai.openai.api-key=...
```

or:

``` text
mongodb+srv://username:password@...
```

inside source-controlled files.

Use environment variables or external configuration instead.

For example:

``` properties
spring.data.mongodb.uri=${MONGODB_URI}
```

and configure `MONGODB_URI` in your environment.

------------------------------------------------------------------------

# 🗺️ Future Enhancements

The project can be extended in the following stages:

### Phase 1 --- Basic Chatbot

-   [x] Spring Boot REST API
-   [x] Spring AI integration
-   [x] Ollama integration
-   [x] Basic chat UI

### Phase 2 --- Conversation

-   [ ] Save messages to MongoDB
-   [ ] Add `sessionId`
-   [ ] Implement chat history
-   [ ] Implement conversation memory

### Phase 3 --- Advanced AI

-   [ ] Structured AI output
-   [ ] Prompt templates
-   [ ] RAG
-   [ ] PDF/document question answering
-   [ ] Embeddings
-   [ ] Vector database
-   [ ] Tool/function calling

### Phase 4 --- Production Application

-   [ ] Spring Security
-   [ ] JWT authentication
-   [ ] User accounts
-   [ ] Chat history per user
-   [ ] React frontend
-   [ ] Docker
-   [ ] CI/CD
-   [ ] Cloud deployment

------------------------------------------------------------------------

# 📚 Useful Commands

### Ollama

``` bash
ollama list
ollama pull llama3.2
ollama run llama3.2
```

### Maven

``` bash
mvn clean
mvn test
mvn package
mvn spring-boot:run
```

### Java

``` bash
java -version
```

------------------------------------------------------------------------

# 📖 Documentation

-   Spring Boot Documentation
-   Spring AI Documentation
-   Spring Data MongoDB Documentation
-   Ollama Documentation
-   Apache Maven Documentation

------------------------------------------------------------------------

## 👨‍💻 Author

**Piyush Kumar**

Spring AI Chat Application\
Built with Java, Spring Boot, Spring AI, Ollama, and MongoDB.
