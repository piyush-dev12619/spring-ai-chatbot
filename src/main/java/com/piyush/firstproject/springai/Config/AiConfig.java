package com.piyush.firstproject.springai.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.mongo.MongoChatMemoryRepository;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

	@Bean
	public ChatMemory chatMemory(MongoChatMemoryRepository repository) {

		return MessageWindowChatMemory
				.builder()
				.chatMemoryRepository(repository)
				.build();
	}

	@Bean
	public ChatClient Chatclient(
			ChatClient.Builder builder,
			ChatMemory chatMemory) {

		return builder
				.defaultAdvisors(
						MessageChatMemoryAdvisor
								.builder(chatMemory)
								.build(),

						new SimpleLoggerAdvisor(),

						new SafeGuardAdvisor(
								List.of("games")
						)
				)
				.defaultOptions(
						OllamaChatOptions
								.builder()
								.model("llama3.2")
								.maxTokens(300)

				)
				.build();
	}


}