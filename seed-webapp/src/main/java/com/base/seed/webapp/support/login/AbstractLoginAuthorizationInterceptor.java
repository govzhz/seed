package com.base.seed.webapp.support.login;

import com.base.seed.common.SignUtil;
import com.base.seed.common.constant.RedisConstants;
import com.base.seed.common.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆拦截器
 *
 *      1. 实现AbstractLoginAuthorizationInterceptor抽象类，实现getRequestParam属性注入规则
 *      2. 将实现类设置拦截器
 *      3. 使用 @Login 注解开启校验（方法或类上），可以通过 [实现类].getUid()获取当前用户id
 *
 *  登陆时需要通过TokenManager.setToken(uid)来设置token
 *
 * @author zz 2018/6/13.
 */
@Slf4j
public abstract class AbstractLoginAuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenManager tokenManager;

    /**
     * 当前用户uid
     */
    private static final ThreadLocal<String> UID_HOLDER = new ThreadLocal<>();

    /**
     * 当前用户token
     */
    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();

    /**
     * 实现类需要实现该方法，提供校验相关参数
     */
    public abstract RequestParam getRequestParamModel(HttpServletRequest request);

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 判断当前连接的是否为业务类的控制器，如果是springmvc静态资源的handler则不拦截
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 如果方法和方法所在的Controller都没有权限校验注解，则不拦截
            if (method.getAnnotation(Login.class) == null
                    && handlerMethod.getBeanType().getAnnotation(Login.class) == null) {
                return true;
            }

            // 获取请求参数
            RequestParam requestParam = getRequestParamModel(request);

            // 判断是否包含timestamp，token，sign参数，如果不含有则返回错误码
            if(StringUtils.isBlank(requestParam.getToken()) ||
                    StringUtils.isBlank(requestParam.getSign()) ||
                    StringUtils.isBlank(requestParam.getTimestamp())){
                throw new LoginException("缺少鉴权参数，请检查token, sign, timestamp是否设置");
            }

            // 判断服务器接到请求的时间和参数中的时间戳相差是否较大(时间戳失效)
            long currentTimestamp = System.currentTimeMillis() / 1000;
            if((currentTimestamp - Long.parseLong(requestParam.getTimestamp())) > RedisConstants.MAX_DIFF_TIME){
                throw new LoginException("时间戳失效");
            }

            // 校验token，查询redis缓存中的uid，如果获取不到则说明该token已过期
            String uid = tokenManager.getByToken(requestParam.getToken());
            if(StringUtils.isBlank(uid)){
                throw new LoginException("token已过期");
            }

            // 校验签名
            Map<String, String> params = new HashMap<>();
            for (Object o : request.getParameterMap().entrySet()) {

                Map.Entry<String, String[]> entry = (Map.Entry)(o);
                String key = entry.getKey();
                String value = entry.getValue()[0];

                if (value.equals(requestParam.getSign())) {
                    continue;
                }
                params.put(key, value);
            }
            String currentSign = SignUtil.createSign(params, true);
            if(!requestParam.getSign().equals(currentSign)){
                throw new LoginException("签名不正确");
            }
            TOKEN_HOLDER.set(requestParam.getToken());
            // TODO: 2019/12/12 设置uid
        } catch (Exception e) {
            log.error("登陆拦截器异常: ", e);
            throw new LoginException(e.getMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清理ThreadLocal变量
        UID_HOLDER.remove();
        TOKEN_HOLDER.remove();
    }

    public static String uid(){
        return UID_HOLDER.get();
    }

    public static String token(){
        return TOKEN_HOLDER.get();
    }
}
