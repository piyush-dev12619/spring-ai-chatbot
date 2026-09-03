package com.piyush.firstproject.springai.ChatController;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping(
            value = "/chat",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> chat(
            @RequestBody String message,
            @RequestParam String conversationId) {

        return chatClient
                .prompt(message)
                .system("""
                        You are a helpful assistant.
                        Explain the query answer in a clean and structured manner.
                        """)
                .advisors(a ->
                        a.param(
                                ChatMemory.CONVERSATION_ID,
                                conversationId
                        )
                )
                .stream()
                .content();
    }
}