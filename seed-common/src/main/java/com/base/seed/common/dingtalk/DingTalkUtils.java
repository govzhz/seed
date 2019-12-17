package com.base.seed.common.dingtalk;

import com.alibaba.fastjson.JSON;
import com.base.seed.common.http.DefaultSingleTonOkHttpClient;
import com.base.seed.common.http.OkHttpWrapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 钉钉群机器人消息发送类
 */
public class DingTalkUtils {

    private static Logger logger = LoggerFactory.getLogger(DingTalkUtils.class);

    private static OkHttpWrapper okHttpWrapper = new OkHttpWrapper(DefaultSingleTonOkHttpClient.getInstance());

    final public static String ERRCODE = "0";

    /**
     * 钉钉群机器人消息
     * ###未抛出和获取发送的结果, 后续需要完善
     *
     * @param url
     * @param msg
     */
    public static void sendMsg(String url, String msg) {
        try {
            okHttpWrapper.syncPostJson(url, msg);
        } catch (IOException e) {
            logger.error("发送钉钉机器人消息失败, URL: {},消息内容：{}", url, msg);
        }
    }


    /**
     * 钉钉群机器人消息
     *
     * @param url
     */
    public static DingTalkResponse sendTextMsg(String url, String content, List<String> atMobiles, boolean isAtAll) throws IOException {

        String msg = getTextMsg(content, atMobiles, isAtAll);

        logger.info("发送钉钉机器人消息,{}", msg);

        String rpcResult = okHttpWrapper.syncPostJson(url, msg);

        logger.info("发送钉钉机器人消息结果,{}", rpcResult);

        return JSON.parseObject(rpcResult, DingTalkResponse.class);
    }

    /**
     * 钉钉群机器人消息, 捕获并忽略异常
     *
     * @param url
     */
    public static void sendTextMsgNoExcption(String url, String content, List<String> atMobiles, boolean isAtAll) {

        String msg = null;
        try {
            msg = getTextMsg(content, atMobiles, isAtAll);

            logger.info("发送钉钉机器人消息,{}", msg);

            String rpcResult = okHttpWrapper.syncPostJson(url, msg);

            logger.info("发送钉钉机器人消息结果,{}", rpcResult);
        }catch (Exception e) {
            logger.error("钉钉消息发送异常,消息内容: {}", msg, e);
        }

    }

    /**
     * 判断是否发送成功
     * @param dingTalkResponse
     * @return
     */
    public static boolean isSuccess(DingTalkResponse dingTalkResponse) {
        if(null == dingTalkResponse) {
            return  false;
        }

        if(StringUtils.equalsIgnoreCase(dingTalkResponse.getErrCode(), ERRCODE)) {
            return  true;
        }
        return false;
    }
    /**
     * 获取群机器人文本消息
     *
     * @param content   发送内容
     * @param atMobiles 需要@发送人的手机号码
     * @param isAtAll   是否@ 所有人
     * @return
     */
    public static String getTextMsg(String content, List<String> atMobiles, boolean isAtAll) {

        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> textMap = Maps.newHashMap();
        Map<String, Object> atMap = Maps.newHashMap();
        paramMap.put("msgtype", "text");
        paramMap.put("text", textMap);
        textMap.put("content", content);
        paramMap.put("at", atMap);
        atMap.put("atMobiles", atMobiles);
        atMap.put("isAtAll", isAtAll);

        return JSON.toJSONString(paramMap);
    }
}
