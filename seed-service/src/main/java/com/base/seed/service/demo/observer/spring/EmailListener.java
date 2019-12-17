package com.base.seed.service.demo.observer.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author zz 2019/12/16
 */
@Service
public class EmailListener implements ApplicationListener<RegisterEvent> {

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        System.out.println(event.getSource() + ": 邮件发送注册成功!");
    }
}
