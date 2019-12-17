package com.base.seed.webapp.observer;

import com.base.seed.service.demo.observer.jdkobserver.BizPublisher;
import com.base.seed.service.demo.observer.jdkobserver.EmailListener;
import com.base.seed.service.demo.observer.jdkobserver.MsgListener;
import com.base.seed.webapp.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zz 2019/12/16
 */
public class ObserverTest extends BaseTest {

    @Autowired
    private BizPublisher bizPublisher;

    @Test
    public void test(){
        bizPublisher.addObserver(new EmailListener());
        bizPublisher.addObserver(new MsgListener());
        bizPublisher.down("It's a new Msg!");
    }
}
