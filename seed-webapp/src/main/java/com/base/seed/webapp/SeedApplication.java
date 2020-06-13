package com.base.seed.webapp;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.base.seed"})
@MapperScan(basePackages = {"com.base.seed.dal"})
@DubboComponentScan(basePackages = {"com.base.seed"})
public class SeedApplication {

  public static void main(String[] args) {
    SpringApplication.run(SeedApplication.class, args);
  }
}
