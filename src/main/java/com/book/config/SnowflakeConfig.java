package com.book.config;

import com.book.common.snowflake.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {

    @Value("${snowflake.node-id:1}")
    private long nodeId;

    @Bean
    public Snowflake snowflakeIdGenerator() {
        return new Snowflake(nodeId);
    }
}
