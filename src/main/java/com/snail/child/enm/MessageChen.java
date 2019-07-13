package com.snail.child.enm;

import com.snail.child.model.Message;

/**
 * Author: 陈一
 * Date: 2019/6/29
 * Description: 陈一的Message
 */
public enum MessageChen implements Message {

    LOGIN_SUCCESS(0, "登录成功！"),
    PASSWORD_WRONG(1, "用户名或密码错误！"),
    GO_CONFIRM(0, "验证邮件已发送，请前往邮箱验证！"),
    USER_EXIST(1, "该邮箱已注册！"),
    REGISTER_SUCCESS(0, "注册成功！"),
    USER_DELETE(0, "用户注销成功！");

    private Integer code;

    private String message;

    MessageChen(Integer code, String message) {
        this.code = code;
        this.message = message;
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

}
