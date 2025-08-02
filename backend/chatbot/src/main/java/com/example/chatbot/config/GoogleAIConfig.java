package com.example.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Configuration
public class GoogleAIConfig {

    @Value("${google.ai.api.key}")
    private String googleAiApiKey;
    @Bean(name = "googleAiApiKeyString")
    public String getGoogleAiApiKey() {
        return googleAiApiKey;
    }
}