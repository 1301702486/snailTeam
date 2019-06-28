package com.snail.child.utils;


import com.snail.child.enm.MsgId;
import com.snail.child.entity.Result;

public class ResultUtils {

    //带code和参数的成功
    public static Result success(MsgId err, Object date){
        Result result = new Result();
        result.setCode(err.getCode());
        result.setMsg(err.getMsg());
        result.setDate(date);
        return result;
    }

    //带参数成功
    public static Result success(Object date){
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功！！");
        result.setDate(date);
        return result;
    }
    //不带参数成功
    public static Result success(){
        return success(null);
    }

    public static Result error(MsgId err){
        Result result = new Result();
        result.setCode(err.getCode());
        result.setMsg(err.getMsg());
        return result;
    }
}
