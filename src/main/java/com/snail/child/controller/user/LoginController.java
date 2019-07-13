package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 陈一
 * Date: 2019/6/27
 * Description: LoginController
 */
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 登录
     *
     * @param emailAddr 用户邮箱地址
     * @param password  密码
     * @return 成功: code=0
     */
    @PostMapping("/login")
    public Result login(@RequestParam("emailAddr") String emailAddr,
                        @RequestParam("password") String password) {
        return loginService.login(emailAddr, password);
    }

}
