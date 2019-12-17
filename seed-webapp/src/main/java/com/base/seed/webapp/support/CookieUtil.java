package com.base.seed.webapp.support;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zz 2018/5/16.
 */
public class CookieUtil {

    public static void setCookie(HttpServletResponse httpServletResponse, String key, String value, int maxAge){
        setCookie(httpServletResponse, key, value, "/", maxAge);
    }

    /**
     * 设置cookie
     */
    public static void setCookie(HttpServletResponse httpServletResponse, String key, String value, String path, int maxAge){
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        if(maxAge > 0){
            cookie.setMaxAge(maxAge);
        }
        httpServletResponse.addCookie(cookie);
    }

    /**
     * 获取cookie
     */
    public static String getCookie(HttpServletRequest httpServletRequest, String key){
        String value = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(key)){
                value = cookie.getValue();
            }
        }
        return value;
    }

    /**
     * 删除cookie
     */
    public static void removeCookie(HttpServletResponse httpServletResponse, String key){
        setCookie(httpServletResponse, key, "", "/", 0);
    }
}
