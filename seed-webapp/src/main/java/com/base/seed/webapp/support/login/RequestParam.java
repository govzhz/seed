package com.base.seed.webapp.support.login;

import lombok.Builder;
import lombok.Data;

/**
 * @author zz 2018/6/13.
 */
@Data
@Builder
public class RequestParam {

    /**
     * token
     */
    String token;

    /**
     * 签名
     */
    String sign;

    /**
     * 时间戳（秒）
     */
    String timestamp;
}
