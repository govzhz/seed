package com.base.seed.webapp.support.handler;

import com.base.seed.common.exception.AccessDeniedException;
import com.base.seed.common.exception.IllegalPasswordException;
import com.base.seed.common.exception.RetryAttemptException;
import com.base.seed.facade.support.ResponseCode;
import com.base.seed.webapp.support.RestEntity;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public RestEntity<Void> handlerException(Exception e) {
    log.warn("Exception: ", e);
    return RestEntity.failed(ResponseCode.SYS_EXCEPTION);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public RestEntity<Void> handlerAccessDeniedException(AccessDeniedException e) {
    log.warn("AccessDeniedException: ", e);
    return RestEntity.failed(ResponseCode.UNAUTHORIZED);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public RestEntity<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    log.warn("HttpMessageNotReadableException: ", ex);
    return RestEntity.failed(ResponseCode.PARAMS_INVALID);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public RestEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    log.warn("MethodArgumentNotValidException: ", ex);
    return getInvalidParamResponse(ex.getBindingResult());
  }

  @ExceptionHandler(BindException.class)
  public RestEntity<Void> handleBindException(BindException ex) {
    log.warn("BindException: ", ex);
    return getInvalidParamResponse(ex.getBindingResult());
  }

  @ExceptionHandler({RetryAttemptException.class})
  public RestEntity<Void> handleRetryAttemptException(RetryAttemptException e) {
    log.info("RetryAttemptException: ", e);
    ResponseCode responseCode = ResponseCode.TRY_ATTEMPT_EXCEEDED;
    switch (e.getRetryAttemptType()) {
      case MODIFY_PASSWORD:
        responseCode = ResponseCode.USER_CHANGE_PWD_ATTEMPT_EXCEEDED;
        break;
      default:
    }
    return RestEntity.failed(responseCode);
  }

  @ExceptionHandler(IllegalPasswordException.class)
  public RestEntity<Void> handleIllegalPasswordException(IllegalPasswordException ex) {
    log.warn("IllegalPasswordException: ", ex);
    return RestEntity.failed(ResponseCode.ILLEGAL_PASSWORD_ERROR);
  }

  private RestEntity<Void> getInvalidParamResponse(BindingResult bindingResult) {
    String message = bindingResult.getFieldErrors().stream().
        map(e -> {
          String msg = e.getDefaultMessage();
          if (!StringUtils.contains(msg, e.getField())) {
            msg = e.getField() + " " + msg;
          }
          return msg;
        }).collect(Collectors.joining(";"));
    return RestEntity.failed(ResponseCode.PARAMS_INVALID.getErrorCode(), message);
  }
}
