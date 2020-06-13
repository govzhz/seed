package com.base.seed.webapp.support.handler;

import com.base.seed.common.exception.AccessDeniedException;
import com.base.seed.facade.support.RespCode;
import com.base.seed.webapp.support.RestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 鉴权失败
   */
  @ExceptionHandler(AccessDeniedException.class)
  public RestEntity<Void> exceptionHandler(AccessDeniedException e) {
    log.warn("AccessDeniedException: ", e);
    return RestEntity.failed(RespCode.UNAUTHORIZED.getCode(), e.getMessage());
  }
}
