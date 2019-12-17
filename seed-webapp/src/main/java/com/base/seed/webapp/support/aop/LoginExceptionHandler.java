package com.base.seed.webapp.support.aop;

import com.base.seed.common.exception.LoginException;
import com.base.seed.facade.support.RespCode;
import com.base.seed.webapp.support.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zz 2019-04-02
 */
@RestControllerAdvice
public class LoginExceptionHandler {

    /**
     * 登录异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public HttpResult<Void> exceptionHandler(LoginException e) {
        return HttpResult.failed(RespCode.UNAUTHORIZED.getCode(), e.getMessage());
    }
}
