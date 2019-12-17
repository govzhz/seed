package com.base.seed.webapp.common;

import com.base.seed.common.dingtalk.DingTalkUtils;
import com.base.seed.webapp.BaseTest;
import org.junit.Test;

/**
 * @author zz 2019-04-08
 */
public class DingTalkUtilsTest extends BaseTest {

    @Test
    public void send() throws InterruptedException {

        DingTalkUtils.sendTextMsgNoExcption("",
                "test"
                , null, false);
    }
}
