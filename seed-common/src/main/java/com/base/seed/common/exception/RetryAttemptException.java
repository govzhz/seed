package com.base.seed.common.exception;

import com.base.seed.common.enums.RetryAttemptType;

public class RetryAttemptException extends RuntimeException {

  private String key;
  private RetryAttemptType retryAttemptType;

  public RetryAttemptException(String message, String key, RetryAttemptType retryAttemptType) {
    super(message);
    this.key = key;
    this.retryAttemptType = retryAttemptType;
  }

  public String getKey() {
    return key;
  }

  public RetryAttemptType getRetryAttemptType() {
    return retryAttemptType;
  }
}
