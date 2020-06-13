package com.base.seed.webapp.support;

import com.base.seed.facade.support.RespCode;
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
   * @see RespCode
   */
  private final Integer code;

  /**
   * 返回信息
   */
  private final String message;

  /**
   * 返回数据
   */
  private final T data;

  /**
   * 记录总数
   */
  private final Long total;

  public static <T> RestEntity<T> success() {
    return RestEntity.<T>builder()
        .code(RespCode.SUCCESS.getCode())
        .message(RespCode.SUCCESS.getMessage())
        .build();
  }

  public static <T> RestEntity<T> success(T data) {
    return RestEntity.<T>builder()
        .code(RespCode.SUCCESS.getCode())
        .message(RespCode.SUCCESS.getMessage())
        .data(data)
        .build();
  }

  public static <T> RestEntity<T> success(T data, long total) {
    return RestEntity.<T>builder()
        .code(RespCode.SUCCESS.getCode())
        .message(RespCode.SUCCESS.getMessage())
        .data(data)
        .total(total)
        .build();
  }

  public static <T> RestEntity<T> failed(RespCode failedCodeEnum) {
    return RestEntity.<T>builder()
        .code(failedCodeEnum.getCode())
        .message(failedCodeEnum.getMessage())
        .build();
  }

  public static <T> RestEntity<T> failed(Integer code, String desc) {
    return RestEntity.<T>builder()
        .code(code)
        .message(desc)
        .build();
  }
}
