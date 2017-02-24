package com.lsh.base.common.exception;

/**
 * Created by huangdong on 16/8/28.
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = -2918744308754155434L;

    private String businessCode;

    public BusinessException() {
        super();
    }

    public BusinessException(String code) {
        super();
        this.businessCode = code;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.businessCode = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.businessCode = code;
    }

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.businessCode = code;
    }

    public String getCode() {
        return businessCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getName());
        String code = this.getCode();
        if (code != null) {
            builder.append(" [").append(code).append("]");
        }
        String message = getMessage();
        if (message != null) {
            builder.append(": ").append(message);
        }
        return builder.toString();
    }
}