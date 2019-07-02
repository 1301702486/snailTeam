package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.LoginService;
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

    @Autowired
    LoginService service;

    /**
     * 跳转到登录界面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(Model model) {
        List<User> users = service.getAllUsers();
        int userCount = users.size();
        model.addAttribute("userCount", userCount);
        return "user/login";
    }

    /**
     * 登录
     *
     * @param model
     * @param emailAddr
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model, String emailAddr, String password) {
        Result result = service.login(emailAddr, password);
//        model.
        return "index";
    }

    @RequestMapping("/login")
    public Result login(User user) {
        return service.login(user.getEmailAddr(), user.getPassword());
    }

}
