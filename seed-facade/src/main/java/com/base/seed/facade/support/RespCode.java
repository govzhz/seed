package com.base.seed.facade.support;

import lombok.Getter;

@Getter
public enum RespCode {

  /**
   * 成功
   */
  SUCCESS(1, "成功"),

  /**
   * 失败
   */
  FAILED(99, "失败"),

  /**
   * 处理中
   */
  PROCESSING(2, "处理中"),

  /**
   * 订单不存在
   */
  NON_EXISTS(-1, "订单不存在"),

  /**
   * 系统异常
   */
  SYS_EXCEPTION(1001, "系统异常"),

  /**
   * 参数不合法
   */
  PARAMS_INVALID(1999, "参数不合法"),

  /**
   * 鉴权失败
   */
  UNAUTHORIZED(2001, "鉴权失败");

  private final Integer code;
  private final String message;

  RespCode(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
}
