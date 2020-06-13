package com.base.seed.service.demo.observer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class RegisterPublisher implements ApplicationEventPublisherAware {

  private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void register(String name) {
    System.out.println(name + ": 注册成功");
    applicationEventPublisher.publishEvent(new RegisterEvent(name));
  }
}
