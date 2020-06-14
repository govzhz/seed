package com.base.seed.common.enums;

import java.util.concurrent.TimeUnit;
import lombok.Getter;

@Getter
public enum RetryAttemptType {

  /**
   * 修改密码操作允许在 1 天内失败 10 次
   */
  MODIFY_PASSWORD("modify_password", "修改密码", 10L, 1L, TimeUnit.DAYS);

  /**
   * 操作唯一标志 需要确保唯一性，将会将其纳入 RedisKey
   */
  private final String code;

  /**
   * 操作描述
   */
  private final String desc;

  /**
   * 最大操作次数
   */
  private final Long maxOperationCnt;

  /**
   * 持续时间
   */
  private final Long duration;

  /**
   * 时间单位
   */
  private final TimeUnit timeUnit;

  RetryAttemptType(String code, String desc, Long maxOperationCnt, Long duration,
      TimeUnit timeUnit) {
    this.code = code;
    this.desc = desc;
    this.maxOperationCnt = maxOperationCnt;
    this.duration = duration;
    this.timeUnit = timeUnit;
  }
}
