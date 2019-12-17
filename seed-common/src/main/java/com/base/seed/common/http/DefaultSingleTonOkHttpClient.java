package com.base.seed.common.http;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * HttpClient单例类
 *
 *      使用该client会共用同一个连接池和调度池，以下几种场景不推荐使用该单例：
 *
 *          1. 存在不一致的超时设置。这时仍然可以共用连接池
 *          2. 存在不一样的连接池策略。这种时候建议根据新的连接池策略新建HttpClient
 *          3. 使用异步请求。这时建议手动设置调度器的最大连接数以及连接超时时间，其中：
 *                  1. connectTimeout可以相对较短
 *                  2. 适当增大maxRequests和maxRequestsPerHost
 *
 * @author zz 2019-04-05
 */
public class DefaultSingleTonOkHttpClient {

    private static class DefaultOkHttpClientHolder {

        private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 60, TimeUnit.SECONDS))
                .retryOnConnectionFailure(true)
                .build();
    }

    public static OkHttpClient getInstance(){
        return DefaultOkHttpClientHolder.CLIENT;
    }

}
