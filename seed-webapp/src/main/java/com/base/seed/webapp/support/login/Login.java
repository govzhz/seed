package com.base.seed.webapp.support.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登陆权限校验
 *
 * 使用该注解的所有方法需要传入 sign, timestamp, token 三个参数用于权限校验
 *
 * @author zz 2018/6/13.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
