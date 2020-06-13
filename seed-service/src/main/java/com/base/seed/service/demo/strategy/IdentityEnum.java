package com.base.seed.service.demo.strategy;

import lombok.Getter;

@Getter
public enum IdentityEnum {

  VIP(1, "VIP用户"),
  NORMAL(2, "普通用户");

  private final Integer code;
  private final String msg;

  IdentityEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
