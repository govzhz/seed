package com.base.seed.service.demo.observer.jdkobserver;

import org.springframework.stereotype.Service;

import java.util.Observable;

/**
 * @author zz 2019/12/16
 */
@Service
public class BizPublisher extends Observable {

    public void down(String msg){
        setChanged();
        notifyObservers(msg);
    }
}
