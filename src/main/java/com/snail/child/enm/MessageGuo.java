package com.snail.child.enm;

import com.snail.child.model.Message;

public enum MessageGuo implements Message {
    NULL_EADDR_AND_PWD_AND_ADDR(201, "必须输入邮箱地址和密码"),
    NO_EMAIL_ADDRESS(202, "邮箱地址不存在"),
    NULL_PROVINCE(203, "省份不存在"),
    CHANGE_PWD_FAILED(204, "密码修改失败"),
    NO_USER(205, "用户不存在"),
    SUCCESS(0, "成功!"),
    ;

    private Integer code;
    private String msg;

    MessageGuo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }
}
