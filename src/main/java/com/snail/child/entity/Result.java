package com.snail.child.entity;

import java.io.Serializable;

public class Result<T> implements Serializable {
        private  Integer code;
        private String msg;
        private T date;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }
}
