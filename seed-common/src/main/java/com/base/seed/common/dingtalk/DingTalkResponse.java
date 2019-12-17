package com.base.seed.common.dingtalk;

import lombok.Data;

/**
 * 钉钉群机器人返回结果
 */
@Data
public class DingTalkResponse {

    /**
     * 返回码-  0为成功
     */
    private String errCode;

    /**
     * 返回信息- ok 为成功
     */
    private String errMsg;
}
