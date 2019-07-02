package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.LoginService;
import com.snail.child.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: 陈一
 * Date: 2019/6/27
 * Description: LoginController
 */
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService service;

    /**
     * 跳转到登录界面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toLogin")
    public Result toLogin(Model model) {
        return service.getAllUsers();
    }

    /**
     * 登录
     *
     * @param emailAddr
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public Result login(String emailAddr, String password) {
        return service.login(emailAddr, password);
    }

}
