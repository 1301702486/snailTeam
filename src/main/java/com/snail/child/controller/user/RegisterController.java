package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.service.user.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
     * 跳转到注册界面
     *
     * @return
     */
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "user/register";
    }

    /**
     * 注册
     *
     * @param model
     * @param emailAddr
     * @param password
     * @return
     */
    @RequestMapping("/register")
    public String register(Model model, String emailAddr, String password) {
        Result result = service.register(emailAddr, password);
        model.addAttribute("info", result.getMessage());
        return "info";
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
                                  @RequestParam(value = "password") String password){
        Result result=service.registerConfirm(emailAddr,password);
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
    @RequestMapping("/testForLoginSuccess")
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
