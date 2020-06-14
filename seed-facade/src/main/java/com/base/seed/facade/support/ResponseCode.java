package com.base.seed.facade.support;

import lombok.Getter;

@Getter
public enum ResponseCode {

  SUCCESS("00000", "成功"),

  // A 错误来源为用户
  PARAMS_INVALID("A0001", "参数不合法"),
  UNAUTHORIZED("A0002", "用户身份验证失败"),
  TRY_ATTEMPT_EXCEEDED("A0003", "操作次数超限"),
  ORDER_OPERATOR_ERROR("A1001", "订单操作失败"),
  ORDER_STATUS_PROCESSING("A1002", "订单处理中"),
  ORDER_STATUS_FAILED("A1003", "订单处理失败"),
  USER_CHANGE_PWD_ATTEMPT_EXCEEDED("A2001", "用户修改密码尝试次数超限"),
  ILLEGAL_PASSWORD_ERROR("A2002", "用户密码错误"),

  // B 错误来源为当前系统
  SYS_EXCEPTION("B0001", "系统执行出错"),

  //C 错误来源为第三方服务
  CLIENT_ERROR("C0001", "调用第三方服务出错");

  private final String errorCode;
  private final String errorMessage;

  ResponseCode(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
}
