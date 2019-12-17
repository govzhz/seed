package com.base.seed.service.demo.observer.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author zz 2019/12/16
 */
@Service
public class MsgListener implements ApplicationListener<RegisterEvent> {

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        System.out.println(event.getSource() + ": 短信发送注册成功!");
    }
}
