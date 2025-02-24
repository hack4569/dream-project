package com.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Value("${aladin.host}")
    public String aladinHost;

    @Value("${gpt.host}")
    public String gptHost;

    @Bean
    public WebClient aladinApi() {
        return WebClient.create(aladinHost);
    }

    @Bean
    public WebClient gptApi() {return WebClient.create(gptHost);}
}
