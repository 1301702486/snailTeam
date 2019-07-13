package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author: 陈一
 * Date: 2019/6/27
 * Description: RegisterController
 */
@RestController
public class RegisterController {

    @Autowired
    RegisterService service;

    /**
     * 发送验证链接
     *
     * @param emailAddr 注册邮箱地址
     * @param password  密码
     * @return 成功: code=0
     */
    @PostMapping("/sendLink")
    public Result sendLink(String emailAddr, String password) {
        return service.sendConfirmLink(emailAddr, password);
    }

    /**
     * 发送邮件验证码
     *
     * @param emailAddr 邮箱地址
     * @return 成功: code=0
     * @author 郭瑞景
     */
    @PostMapping("/sendCode")
    public Result sendCode(String emailAddr) {
        return service.sendCode(emailAddr);
    }

    /**
     * 注册
     *
     * @param emailAddr 邮箱地址
     * @param password  密码
     * @return 成功: code=0
     * @author 郭瑞景
     */
    @PostMapping("/register")
    public Result register(String emailAddr, String password) {
        return service.register(emailAddr, password);
    }
}
