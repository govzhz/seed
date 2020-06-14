package com.base.seed.webapp.properties;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@JSONType(naming = PropertyNamingStrategy.KebabCase)
@Data
public class RedisProperties {

  private String sentinels;
  private String password;
  private Integer database;
  private String masterName;
  private Integer timeout;
  private Pool pool;
  private Boolean testOnBorrow;

  @Data
  @JSONType(naming = PropertyNamingStrategy.KebabCase)
  public static class Pool {

    private Integer maxTotal;
    private Integer maxIdle;
    private Long maxWait;
  }
}
