package com.lsh.base.common.utils;

import java.io.Serializable;

/**
 * Created by 杨爱友 on 16/3/21.
 */
public class RpcResult<T> implements Serializable {
    /**
     * 正常
     */
    public static final Integer SUCCESS = 1;
    /**
     * 参数无效
     */
    public static final Integer PARAM_INVALID = 2;
    /**
     * 系统异常
     */
    public static final Integer EXCEPTION = 3;
    /**
     * 业务异常
     */
    public static final Integer BIZ_EXCEPTION = 4;
    /**
     * 超时异常
     */
    public static final Integer TOKEN_EXCEPTION = 5;
    /**
     * 其他异常
     */
    public static final Integer OTHER_EXCEPTION = 6;

    private Integer  code;
    private String message;
    private T data;

    public static <T> RpcResult<T> SUCCESS() {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(SUCCESS);
        result.setMessage("OK.");
        return result;
    }

    public static <T> RpcResult<T> SUCCESS(T data) {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(SUCCESS);
        result.setMessage("OK.");
        result.setData(data);
        return result;
    }

    public static <T> RpcResult<T> PARAMETER_ERROR() {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(PARAM_INVALID);
        result.setMessage("parameter invalid.");
        return result;
    }

    public static <T> RpcResult<T> PARAMETER_ERROR(String message) {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(PARAM_INVALID);
        result.setMessage(message);
        return result;
    }

    public static <T> RpcResult<T> EXCEPTION_ERROR() {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(EXCEPTION);
        result.setMessage("system exception.");
        return result;
    }

    public static <T> RpcResult<T> EXCEPTION_ERROR(String message) {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(EXCEPTION);
        result.setMessage(message);
        return result;
    }

    public static <T> RpcResult<T> BIZ_ERROR(String message) {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(BIZ_EXCEPTION);
        result.setMessage(message);

        return result;
    }

    public static <T> RpcResult<T> TOKEN_EXCEPTION(String message) {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(TOKEN_EXCEPTION);
        result.setMessage(message);
        return result;
    }


    public static <T> RpcResult<T> OTHER_EXCEPTION(String message) {
        RpcResult<T> result = new RpcResult<T>();
        result.setCode(OTHER_EXCEPTION);
        result.setMessage(message);

        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
