package com.base.seed.webapp.support.handler;

import com.base.seed.common.annos.RetryAttemptChecker;
import com.base.seed.common.enums.RetryAttemptType;
import com.base.seed.common.exception.RetryAttemptException;
import com.base.seed.service.RetryAttemptService;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RetryAttemptAspect {

  private final ExpressionParser parser = new SpelExpressionParser();
  private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

  @Autowired
  private RetryAttemptService retryAttemptService;

  @Around("@annotation(retryAttemptChecker)")
  public Object invoke(ProceedingJoinPoint pjp, RetryAttemptChecker retryAttemptChecker) throws Throwable {
    String key = parseKey(retryAttemptChecker.key(), pjp);
    boolean locked = retryAttemptService.isLocked(key, retryAttemptChecker.retryAttemptType());
    if (locked) {
      throw new RetryAttemptException(
          String.format("Key[%S], Operator[%s] is locked", key, retryAttemptChecker.retryAttemptType()), key,
          retryAttemptChecker.retryAttemptType());
    }

    Object res;
    try {
      res = pjp.proceed(pjp.getArgs());
    } catch (Throwable e) {
      retryForException(key, retryAttemptChecker.retryAttemptType(), retryAttemptChecker.retryForException(), e);
      throw e;
    }

    retryForSuccess(key, retryAttemptChecker);
    return res;
  }

  private void retryForSuccess(String key, RetryAttemptChecker retryAttemptChecker) {
    if (ArrayUtils.isNotEmpty(retryAttemptChecker.retryForException())) {
      return;
    }
    retryAttemptService.retry(key, retryAttemptChecker.retryAttemptType());
  }

  private void retryForException(String key, RetryAttemptType retryAttemptType,
      Class<? extends Throwable>[] exceptions, Throwable e) {
    boolean retry = Arrays.stream(exceptions).anyMatch(retryFor -> retryFor.isInstance(e));
    if (retry) {
      retryAttemptService.retry(key, retryAttemptType);
    }
  }

  private String parseKey(String key, ProceedingJoinPoint pjp) {
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    String[] paramNames = discoverer.getParameterNames(methodSignature.getMethod());
    if (paramNames == null) {
      throw new RuntimeException("Parse SpEl error, paramNames is null");
    }

    Expression expression = parser.parseExpression(key);
    EvaluationContext context = new StandardEvaluationContext();
    Object[] args = pjp.getArgs();
    for (int i = 0; i < args.length; i++) {
      context.setVariable(paramNames[i], args[i]);
    }

    Object res = Optional.ofNullable(expression.getValue(context))
        .orElseThrow(() -> new RuntimeException("Parse SpEl error, key is null"));
    return res.toString();
  }
}
