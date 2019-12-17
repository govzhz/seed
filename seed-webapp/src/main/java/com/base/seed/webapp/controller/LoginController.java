package com.base.seed.webapp.controller;

import com.base.seed.facade.support.RespCode;
import com.base.seed.webapp.support.HttpResult;
import com.base.seed.webapp.support.login.DefaultLoginAuthorizationInterceptor;
import com.base.seed.webapp.support.login.Login;
import com.base.seed.webapp.support.login.TokenManager;
import com.base.seed.webapp.vo.AuthorizationVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zz 2019/12/12
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public HttpResult<AuthorizationVo> login(String userName, String passWord){

        log.info("用户登陆操作: username={}, password={}", userName, passWord);

        try {
            Assert.isTrue(StringUtils.isNoneBlank(userName, passWord), "用户名账户或密码为空");

            String uid = getUid(userName, passWord);
            String token = tokenManager.setToken(uid);
            return HttpResult.success(new AuthorizationVo(token, uid));
        } catch (Exception e){
            log.error("用户登陆异常: ", e);
            return HttpResult.failed(RespCode.PARAMS_INVALID);
        }
    }

    @Login
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public HttpResult<Void> logout(){

        log.info("用户[{}]登出操作", DefaultLoginAuthorizationInterceptor.uid());

        try {
            tokenManager.removeToken(DefaultLoginAuthorizationInterceptor.token());
        } catch (Exception e){
            log.error("用户登出异常: ", e);
            return HttpResult.failed(RespCode.SYS_EXCEPTION);
        }
        return HttpResult.success();
    }

    private String getUid(String userName, String passWord){
        // TODO: 2019/12/12 账户密码校验
        return userName;
    }
}
