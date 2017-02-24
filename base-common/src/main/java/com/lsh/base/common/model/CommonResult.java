package com.lsh.base.common.model;

import java.io.Serializable;

/**
 * Created by huangdong on 16/8/28.
 */
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 7987787495220498301L;

    public static String SUCCESS = "0000";

    public static String ERROR = "E9999";

    private String code;

    private String message;

    private T data;

    private final long timestamp = System.currentTimeMillis();

    public CommonResult() {
    }

    public CommonResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public static CommonResult<?> success() {
        return success(null, "success");
    }

    public static <T> CommonResult<T> success(T data) {
        return success(data, "success");
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(SUCCESS, message, data);
    }

    public static CommonResult<?> error() {
        return error("error");
    }

    public static CommonResult<?> error(String message) {
        return error(message, null);
    }

    public static <T> CommonResult<T> error(String message, T data) {
        return new CommonResult<T>(ERROR, message, data);
    }
}
