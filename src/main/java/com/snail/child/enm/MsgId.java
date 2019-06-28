package com.snail.child.enm;

public enum MsgId {
    NULL_EADDR_AND_PWD_AND_ADDR(201,"必须输入邮箱地址和密码"),
    NO_EMAIL_ADDRESS(202,"邮箱地址不存在"),
    NULL_PROVINCE(203,"省份不存在"),
    ;


    private Integer code;
    private String msg;

    MsgId(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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
}
