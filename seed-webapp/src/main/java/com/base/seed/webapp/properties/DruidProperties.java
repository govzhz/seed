package com.base.seed.webapp.properties;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
@JSONType(naming = PropertyNamingStrategy.KebabCase)
@Data
public class DruidProperties {

  private String url;
  private String username;
  private String password;
  private String driverClassName;
  private int initialSize;
  private int maxActive;
  private int minIdle;
  private int maxWait;
  private boolean poolPreparedStatements;
  private int maxPoolPreparedStatementPerConnectionSize;
  private int timeBetweenEvictionRunsMillis;
  private int minEvictableIdleTimeMillis;
  private int maxEvictableIdleTimeMillis;
  private String validationQuery;
  private boolean testWhileIdle;
  private boolean testOnBorrow;
  private boolean testOnReturn;
  private String filters;
  private String connectionProperties;
}
