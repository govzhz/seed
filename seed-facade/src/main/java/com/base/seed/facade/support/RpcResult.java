package com.base.seed.facade.support;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Rpc响应类
 *
 * @see com.base.seed.facade.support.Page
 */
@Builder
@Getter
@Setter
@ToString
public final class RpcResult<T> implements Serializable {

  private static final long serialVersionUID = 2794072190565213267L;

  /**
   * 返回码
   */
  private String errorCode;

  /**
   * 返回信息
   */
  private String errorMessage;

  /**
   * 返回数据
   */
  private T data;

  public boolean isSuccess() {
    return errorCode.equals(ResponseCode.SUCCESS.getErrorCode());
  }

  public static <T> RpcResult<T> ok(T data) {
    return RpcResult.<T>builder()
        .errorCode(ResponseCode.SUCCESS.getErrorCode())
        .errorMessage(ResponseCode.SUCCESS.getErrorMessage())
        .data(data)
        .build();
  }

  public static <T> RpcResult<T> ok() {
    return RpcResult.<T>builder()
        .errorCode(ResponseCode.SUCCESS.getErrorCode())
        .errorMessage(ResponseCode.SUCCESS.getErrorMessage())
        .build();
  }

  public static <T> RpcResult<T> failed(String code, String desc) {
    return RpcResult.<T>builder()
        .errorCode(code)
        .errorMessage(desc)
        .build();
  }

  public static <T> RpcResult<T> failed(ResponseCode responseCode) {
    return RpcResult.<T>builder()
        .errorCode(responseCode.getErrorCode())
        .errorMessage(responseCode.getErrorMessage())
        .build();
  }
}
