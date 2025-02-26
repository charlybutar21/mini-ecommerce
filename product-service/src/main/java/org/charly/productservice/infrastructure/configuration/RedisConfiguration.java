package org.charly.productservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
