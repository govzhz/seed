package com.base.seed.webapp.support;

import com.base.seed.facade.support.RespCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Http响应类
 *
 * @author zz 2019-03-22
 */
@Builder
@Getter
@ToString
public final class HttpResult<T> implements Serializable {

    private static final long serialVersionUID = -8064133049842101875L;

    /**
     * 返回码
     *
     * @see RespCode
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 记录总数
     */
    private Long total;

    public static <T> HttpResult<T> success(){
        return HttpResult.<T>builder()
                .code(RespCode.SUCCESS.getCode())
                .message(RespCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> HttpResult<T> success(T data){
        return HttpResult.<T>builder()
                .code(RespCode.SUCCESS.getCode())
                .message(RespCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> HttpResult<T> success(T data, long total){
        return HttpResult.<T>builder()
                .code(RespCode.SUCCESS.getCode())
                .message(RespCode.SUCCESS.getMessage())
                .data(data)
                .total(total)
                .build();
    }

    public static <T> HttpResult<T> failed(RespCode failedCodeEnum){
        return HttpResult.<T>builder()
                .code(failedCodeEnum.getCode())
                .message(failedCodeEnum.getMessage())
                .build();
    }

    public static <T> HttpResult<T> failed(Integer code, String desc){
        return HttpResult.<T>builder()
                .code(code)
                .message(desc)
                .build();
    }
}
