package com.base.seed.webapp.support.handler;

import com.base.seed.common.exception.AccessDeniedException;
import com.base.seed.facade.support.RespCode;
import com.base.seed.webapp.support.RestEntity;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestEntity<Void>> handlerException(Exception e) {
    log.warn("Exception: ", e);
    return ResponseEntity.status(HttpStatus.OK)
        .body(RestEntity.failed(RespCode.SYS_EXCEPTION));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<RestEntity<Void>> handlerAccessDeniedException(AccessDeniedException e) {
    log.warn("AccessDeniedException: ", e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(RestEntity.failed(RespCode.UNAUTHORIZED));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestEntity<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    log.warn("MethodArgumentNotValidException: ", ex);
    return getInvalidParamResponse(ex.getBindingResult());
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<RestEntity<Void>> handleBindException(BindException ex) {
    log.warn("BindException: ", ex);
    return getInvalidParamResponse(ex.getBindingResult());
  }

  private ResponseEntity<RestEntity<Void>> getInvalidParamResponse(BindingResult bindingResult) {
    String message = bindingResult.getFieldErrors().stream().
        map(e -> {
          String msg = e.getDefaultMessage();
          if (!StringUtils.contains(msg, e.getField())) {
            msg = e.getField() + " " + msg;
          }
          return msg;
        }).collect(Collectors.joining("；"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(RestEntity.failed(RespCode.PARAMS_INVALID.getCode(), message));
  }
}
