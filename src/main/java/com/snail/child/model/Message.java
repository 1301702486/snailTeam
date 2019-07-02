package com.snail.child.model;

/**
 * Author: 陈一
 * Date: 2019/6/29
 * Description: 用于传递信息
 */
public interface Message {

    Integer getCode();

    void setCode(Integer code);

    String getMessage();

    void setMessage(String message);

}
