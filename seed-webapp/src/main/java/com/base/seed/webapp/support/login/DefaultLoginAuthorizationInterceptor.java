package com.base.seed.webapp.support.login;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zz 2019/12/12
 */
@Service
public class DefaultLoginAuthorizationInterceptor extends AbstractLoginAuthorizationInterceptor {

    @Override
    public RequestParam getRequestParamModel(HttpServletRequest request) {
        return RequestParam.builder()
                .token(request.getParameter("token"))
                .timestamp(request.getParameter("timestamp"))
                .sign(request.getParameter("sign"))
                .build();
    }
}
