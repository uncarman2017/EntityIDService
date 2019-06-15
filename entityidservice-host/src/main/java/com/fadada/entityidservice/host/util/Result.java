package com.fadada.entityidservice.host.util;

import java.io.Serializable;

/**
 * int code，接口调用成功=0，错误码=其他值
 * T object，具体返回值
 * String error，字符串错误码，可选
 * String message，错误消息，可选，error=message可以配置成属性文件
 * Exception exception，异常消息，可选
 */
public class Result<T> implements Serializable {

    private int code;
    private T object;
    private String error;
    private String message;
    private Exception exception;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

}
