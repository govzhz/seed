package com.base.seed.webapp.support;

import com.base.seed.facade.support.ResponseCode;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public final class RestEntity<T> implements Serializable {

  private static final long serialVersionUID = -8064133049842101875L;

  /**
   * 返回码
   *
   * @see ResponseCode
   */
  private final String errorCode;

  /**
   * 返回信息
   */
  private final String errorMessage;

  /**
   * 返回数据
   */
  private final T data;

  /**
   * 记录总数
   */
  private final Long total;

  public static <T> RestEntity<T> ok() {
    return RestEntity.<T>builder()
        .errorCode(ResponseCode.SUCCESS.getErrorCode())
        .errorMessage(ResponseCode.SUCCESS.getErrorMessage())
        .build();
  }

  public static <T> RestEntity<T> ok(T data) {
    return RestEntity.<T>builder()
        .errorCode(ResponseCode.SUCCESS.getErrorCode())
        .errorMessage(ResponseCode.SUCCESS.getErrorMessage())
        .data(data)
        .build();
  }

  public static <T> RestEntity<T> ok(T data, long total) {
    return RestEntity.<T>builder()
        .errorCode(ResponseCode.SUCCESS.getErrorCode())
        .errorMessage(ResponseCode.SUCCESS.getErrorMessage())
        .data(data)
        .total(total)
        .build();
  }

  public static <T> RestEntity<T> failed(ResponseCode failedCode) {
    return RestEntity.<T>builder()
        .errorCode(failedCode.getErrorCode())
        .errorMessage(failedCode.getErrorMessage())
        .build();
  }

  public static <T> RestEntity<T> failed(String code, String desc) {
    return RestEntity.<T>builder()
        .errorCode(code)
        .errorMessage(desc)
        .build();
  }
}
