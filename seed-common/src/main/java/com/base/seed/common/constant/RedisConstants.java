package com.base.seed.common.constant;

/**
 * @author zz 2019/12/11
 */
public class RedisConstants {

    /**
     * token过期时间（秒）
     */
    public static final Long TOKEN_EXPIRE = 60 * 5L;

    /**
     * 请求时间戳和系统当前时间戳可接受最大差值（秒）
     */
    public static final Long MAX_DIFF_TIME = 30 * 60L;
}
