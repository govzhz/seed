package com.base.seed.webapp.support.aop;

import com.base.seed.facade.support.RespCode;
import com.base.seed.webapp.support.HttpResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    /*@Around("execution(* com.base.seed.webapp.controller..*Controller.*(..)) && !execution(* com.base.seed.webapp" + ""
            + ".controller.StatusController.*(..))")
    public Object handleSysError(ProceedingJoinPoint pjp) {
        String inf = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        String methodSignature = inf + "." + methodName;
        logger.info("rest enter method: {}, args: {}", methodSignature, Arrays.toString(pjp.getArgs()));
        Object retVal;
        try {
            retVal = pjp.proceed();
            return retVal;
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            if (CollectionUtil.isNotEmpty(violations)) {
                String errMsg = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
                return HttpResult.<Void>failed(RespCode.FAIL.getCode(), errMsg);
            }
            return HttpResult.<Void>failed(RespCode.FAIL.getCode(), "请求参数错误");
        } catch (IllegalArgumentException e) {
            logger.error(String.format("rest executed method: %s failed: {}", methodSignature), e.getMessage());
            return HttpResult.<Void>failed(RespCode.FAIL.getCode(), e.getMessage());
        } catch (Throwable e) {
            logger.error(String.format("rest executed method: %s failed", methodSignature), e);
            return HttpResult.<Void>failed(RespCode.SYS_EXCEPTION);
        }
    }*/
}
