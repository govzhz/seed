package com.base.seed.common.properties;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "registry.zookeeper")
@JSONType(naming = PropertyNamingStrategy.KebabCase)
@Data
public class ZookeeperProperties {

  private String serverLists;
  private int baseSleepTimeMilliseconds = 1000;
  private int maxSleepTimeMilliseconds = 3000;
  private int maxRetries = 3;
  private String namespace;
}
