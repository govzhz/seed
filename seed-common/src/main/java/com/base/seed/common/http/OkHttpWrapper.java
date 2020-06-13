package com.base.seed.common.http;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpWrapper {

  private static final MediaType APPLICATION_JSON_UTF8 = MediaType.parse("application/json;charset=UTF-8");

  private OkHttpClient client;

  public OkHttpWrapper(OkHttpClient client) {
    this.client = client;
  }

  public Object syncGet(String url, Class<?> tClass) throws IOException {
    return JSON.parseObject(syncGet(url), tClass);
  }

  public Object syncPostJson(String url, String json, Class<?> tClass) throws IOException {
    return JSON.parseObject(syncPostJson(url, json), tClass);
  }

  public Object syncPostForm(String url, FormBody formBody, Class<?> tClass) throws IOException {
    return JSON.parseObject(syncPostForm(url, formBody), tClass);
  }

  public String syncGet(String url) throws IOException {

    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
      return response.body().string();
    }
  }

  public String syncPostForm(String url, FormBody formBody) throws IOException {

    Request request = new Request.Builder()
        .url(url)
        .post(formBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
      return response.body().string();
    }
  }

  public String syncPostJson(String url, String json) throws IOException {

    Request request = new Request.Builder()
        .url(url)
        .post(RequestBody.create(APPLICATION_JSON_UTF8, json))
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
      return response.body().string();
    }
  }

  /**
   * 异步请求
   *
   *      注意对response响应需要关闭 https://square.github.io/okhttp/3.x/okhttp/okhttp3/ResponseBody.html
   *      e.g.
   *          CallbackFuture future = asyncGet(url)
   *          Response response = future.get();
   *
   *          // 1. string() will auto close
   *          response.body().string()
   *
   *          // 2. try https://github.com/square/okhttp/wiki/Recipes
   *          try (ResponseBody responseBody = response.body()) {
   *
   *          }
   * @param url
   */
  public CallbackFuture asyncGet(String url) {

    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    return asyncRequest(request, new CallbackFuture());
  }

  public CallbackFuture asyncPostJson(String url, String json) {

    Request request = new Request.Builder()
        .url(url)
        .post(RequestBody.create(APPLICATION_JSON_UTF8, json))
        .build();

    return asyncRequest(request, new CallbackFuture());
  }

  public CallbackFuture asyncPostForm(String url, FormBody formBody) {

    Request request = new Request.Builder()
        .url(url)
        .post(formBody)
        .build();

    return asyncRequest(request, new CallbackFuture());
  }

  private CallbackFuture asyncRequest(Request request, CallbackFuture callbackFuture) {
    client.newCall(request).enqueue(callbackFuture);
    return callbackFuture;
  }
}
