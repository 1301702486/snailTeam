package com.snail.child.utils;

import com.snail.child.model.Message;
import com.snail.child.model.Result;

public class ResultUtils {

    public static Result send(Message message, Object... data) {
        Result result = new Result();
        result.setCode(message.getCode());
        result.setMessage(message.getMessage());
        result.setData(data);
        System.out.println(message.getMessage());
        return result;
    }

    public static Result send(Message message) {
        Result result = new Result();
        result.setCode(message.getCode());
        result.setMessage(message.getMessage());
        System.out.println(message.getMessage());
        return result;
    }
}
