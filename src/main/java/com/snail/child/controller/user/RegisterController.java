package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: 陈一
 * Date: 2019/6/27
 * Description: RegisterController
 */
@RestController
public class RegisterController {

    @Autowired
    RegisterService service;


    @PostMapping("/register")
    public Result register(String emailAddr, String password, HttpServletRequest request) {
        Result result = service.register(emailAddr, password);
        HttpSession session = request.getSession();
        session.setAttribute("userName", emailAddr);
        session.setAttribute("password", password);
        return result;
    }

    /**
     * 注册验证
     *
     * @param model
     * @param emailAddr
     * @return
     */
    public String registerConfirm(Model model,
                                  @RequestParam(value = "emailAddr") String emailAddr,
                                  @RequestParam(value = "password") String password) {
        Result result = service.registerConfirm(emailAddr, password);
        model.addAttribute("info", result.getMessage());
        return "index";
    }

    /**
     * 添加带头像的用户，以便测试“登录成功”
     *
     * @param emailAddr
     * @param password
     * @param file
     * @return
     */
    @PostMapping("/testForLoginSuccess")
    public Result testForLoginSuccess(@RequestParam(value = "emailAddr") String emailAddr,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "headPortrait") MultipartFile file) {
        return service.testForLoginSuccess(emailAddr, password, file);
    }

    /**
     * 删除用户
     *
     * @param emailAddr
     * @return
     */
    @RequestMapping("/deleteUser")
    public Result deleteUser(@RequestParam(value = "emailAddr") String emailAddr) {
        return service.deleteUser(emailAddr);
    }

}
