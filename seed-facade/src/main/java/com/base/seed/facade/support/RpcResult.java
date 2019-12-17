package com.base.seed.facade.support;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Rpc响应类
 *
 *      分页结合 @see com.base.seed.facade.support.Page 使用，RpcResult<Page<T>>
 *
 * @author zz 2019-08-08
 */
@Builder
@Getter
@Setter
@ToString
public final class RpcResult<T> implements Serializable {

    private static final long serialVersionUID = 2794072190565213267L;

    /**
     * 返回码
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

    public boolean isSuccess(){
        return code.equals(RespCode.SUCCESS.getCode());
    }

    public static <T> RpcResult<T> success(T data){
        return RpcResult.<T>builder()
                .code(RespCode.SUCCESS.getCode())
                .message(RespCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> RpcResult<T> success(){
        return RpcResult.<T>builder()
                .code(RespCode.SUCCESS.getCode())
                .message(RespCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> RpcResult<T> fail(Integer code, String desc){
        return RpcResult.<T>builder()
                .code(code)
                .message(desc)
                .build();
    }

    public static <T> RpcResult<T> fail(RespCode respCode){
        return RpcResult.<T>builder()
                .code(respCode.getCode())
                .message(respCode.getMessage())
                .build();
    }

    public static <T> RpcResult<T> dependOnCode(RespCode respCode){
        return RpcResult.<T>builder()
                .code(respCode.getCode())
                .message(respCode.getMessage())
                .build();
    }

    public static <T> RpcResult<T> dependOnCode(Integer code, String desc){
        return RpcResult.<T>builder()
                .code(code)
                .message(desc)
                .build();
    }
}
