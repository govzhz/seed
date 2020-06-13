package com.base.seed.service.demo.observer;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class SmsListener implements ApplicationListener<RegisterEvent> {

  @Override
  public void onApplicationEvent(RegisterEvent event) {
    System.out.println(event.getSource() + ": 短信发送注册成功!");
  }
}
