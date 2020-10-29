package com.base.seed.common.exception;

import com.base.seed.facade.support.ResponseCode;
import lombok.Getter;

/**
 * 通用业务异常类
 */
public class BizException extends RuntimeException {

  private static final long serialVersionUID = -5910219797132438456L;

  @Getter
  private ResponseCode errorCode;

  public BizException() {
  }

  public BizException(String message) {
    super(message);
  }

  public BizException(String message, ResponseCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public BizException(Throwable cause) {
    super(cause);
  }

  public BizException(String message, Throwable cause) {
    super(message, cause);
  }
}
