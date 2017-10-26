package com.lanou.utils;

/**
 * Created by dllo on 17/10/20.
 */
public class AjaxResult {

    private Object data;
    private Integer errorCode;
    private String message;

    public AjaxResult(Object data, Integer errorCode, String message) {
        this.data = data;
        this.errorCode = errorCode;
        this.message = message;
    }

    public AjaxResult(String message) {
        this.message = message;
    }

    public AjaxResult(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public AjaxResult(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
