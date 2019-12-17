package com.base.seed.webapp.common;

import com.alibaba.fastjson.JSON;
import com.base.seed.common.http.CallbackFuture;
import com.base.seed.common.http.DefaultSingleTonOkHttpClient;
import com.base.seed.common.http.OkHttpWrapper;
import com.base.seed.webapp.BaseTest;
import okhttp3.FormBody;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author zz 2019-04-06
 */
public class HttpTest extends BaseTest {

    private OkHttpWrapper okHttpWrapper = new OkHttpWrapper(DefaultSingleTonOkHttpClient.getInstance());

    private static final String BASE_URL = "http://httpbin.org/";

    @Test
    public void syncPostForm() throws IOException {

        FormBody formBody = new FormBody.Builder()
                .add("key1", "value1")
                .add("key2", "value2")
                .build();

        String data = okHttpWrapper.syncPostForm(BASE_URL + "post", formBody);

        Assert.assertTrue(StringUtils.isNotBlank(data));
    }

    @Test
    public void syncPostJson() throws IOException {

        Response response = (Response) okHttpWrapper.syncPostJson(BASE_URL + "post", JSON.toJSONString(new Request("value1", "value2")), Response.class);

        Assert.assertTrue(response != null && StringUtils.isNotBlank(response.getUrl()));
    }

    @Test
    public void syncGet() throws IOException {

        Response response = (Response) okHttpWrapper.syncGet(BASE_URL + "get", Response.class);

        Assert.assertTrue(response != null && StringUtils.isNotBlank(response.getUrl()));
    }

    @Test
    public void asyncGet() throws IOException, ExecutionException, InterruptedException {

        CallbackFuture callbackFuture = okHttpWrapper.asyncGet(BASE_URL + "get");

        okhttp3.Response response = callbackFuture.get();

        String data = response.body().string();

        Assert.assertTrue(StringUtils.isNotBlank(data));
    }
}

