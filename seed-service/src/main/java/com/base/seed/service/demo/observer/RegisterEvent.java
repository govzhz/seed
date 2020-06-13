package com.base.seed.service.demo.observer;

import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {

  public RegisterEvent(Object source) {
    super(source);
  }
}
