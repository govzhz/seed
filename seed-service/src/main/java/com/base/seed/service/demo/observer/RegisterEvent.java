package com.base.seed.service.demo.observer;

import org.springframework.context.ApplicationEvent;

/**
 * @author zz 2019/12/16
 */
public class RegisterEvent extends ApplicationEvent {

    public RegisterEvent(Object source) {
        super(source);
    }
}
