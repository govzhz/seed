package com.base.seed.common.annos;

import com.base.seed.common.enums.RetryAttemptType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认策略： 1.方法执行成功后增加重试次数 2. 重试次数大于阈值触发锁定 3. 过期时间到时重置重试次数
 * <p>
 * 重试次数可选策略： 设置 retryForException 将在方法抛出指定异常后增加重试次数
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryAttemptChecker {

  /**
   * 抛出指定异常时进行重试次数累计，默认不对任何异常进行累加
   */
  Class<? extends Throwable>[] retryForException() default {};

  /**
   * 操作人唯一编号，请确保 key + RetryAttemptType.code（Who did what） 全局唯一
   * <p>
   * 支持 SpEL 语法
   */
  String key();

  /**
   * 当前操作重试策略
   */
  RetryAttemptType retryAttemptType();
}
