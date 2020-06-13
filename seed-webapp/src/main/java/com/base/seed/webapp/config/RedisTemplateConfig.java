package com.base.seed.webapp.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisTemplateConfig {

  @Bean
  public <T, E> RedisTemplate<T, E> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<T, E> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

    // 设置默认的Serialize，包含 keySerializer & valueSerializer
    redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);

    // redisTemplate.setKeySerializer(fastJsonRedisSerializer);//单独设置keySerializer
    // redisTemplate.setValueSerializer(fastJsonRedisSerializer);//单独设置valueSerializer
    return redisTemplate;
  }
}
