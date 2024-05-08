package com.example.demo.controller.domain;

public class Result<T> {
    // 返回状态码
    private Integer code;

    // 返回数据
    private T data;

    // 返回异常信息
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
