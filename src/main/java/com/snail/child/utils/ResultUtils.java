package com.snail.child.utils;

import com.snail.child.model.Message;
import com.snail.child.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResultUtils.class);

    public static Result send(Message message, Object data){
        Result result = new Result();
        result.setCode(message.getCode());
        result.setMessage(message.getMessage());
        result.setData(data);
        logger.info(message.getMessage());
        return result;
    }

    public static Result send(Message message){
        Result result = new Result();
        result.setCode(message.getCode());
        result.setMessage(message.getMessage());
        logger.info(message.getMessage());
        return result;
    }

    public static Result send(Object data){
        Result result = new Result();
        result.setData(data);
        logger.info(data.toString());
        return result;
    }
}
