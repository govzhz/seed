package com.base.seed.common.exception;

/**
 * 通用业务异常类
 */
public class BizException extends RuntimeException {

  private static final long serialVersionUID = -5910219797132438456L;

  public BizException() {
  }

  public BizException(String message) {
    super(message);
  }

  public BizException(Throwable cause) {
    super(cause);
  }

  public BizException(String message, Throwable cause) {
    super(message, cause);
  }
}
