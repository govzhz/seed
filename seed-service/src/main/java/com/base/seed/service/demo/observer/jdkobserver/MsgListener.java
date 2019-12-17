package com.base.seed.service.demo.observer.jdkobserver;

import java.util.Observable;
import java.util.Observer;

/**
 * @author zz 2019/12/16
 */
public class MsgListener implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Send Msg: " + arg);
    }
}
