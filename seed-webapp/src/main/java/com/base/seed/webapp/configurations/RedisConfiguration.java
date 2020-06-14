package com.base.seed.webapp.configurations;

import com.base.seed.webapp.properties.RedisProperties;
import java.util.Arrays;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

  @Autowired
  private RedisProperties redisProperties;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    String[] redisSentinels = StringUtils.commaDelimitedListToStringArray(redisProperties.getSentinels());
    SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
    Arrays.stream(redisSentinels)
        .map(sentinel -> "redis://" + sentinel)
        .forEach(sentinelServersConfig::addSentinelAddress);
    sentinelServersConfig.setPassword(redisProperties.getPassword())
        .setDatabase(redisProperties.getDatabase())
        .setMasterName(redisProperties.getMasterName())
        .setConnectTimeout(redisProperties.getTimeout())
        .setReadMode(ReadMode.MASTER);
    config.setMaxCleanUpDelay(30);
    return Redisson.create(config);
  }
}