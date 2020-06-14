package com.base.seed.service;

import com.base.seed.common.enums.RetryAttemptType;
import javax.annotation.Nonnull;

/**
 * 重试次数验证服务
 */
public interface RetryAttemptService {

  /**
   * 是否已被锁定
   */
  boolean isLocked(@Nonnull String unionKey, @Nonnull RetryAttemptType retryAttemptType);

  /**
   * 发起重试
   */
  void retry(@Nonnull String unionKey, @Nonnull RetryAttemptType retryAttemptType);

  /**
   * 清除重试次数
   */
  boolean clear(@Nonnull String unionKey, @Nonnull RetryAttemptType retryAttemptType);
}
