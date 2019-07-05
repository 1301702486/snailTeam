package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Author: 陈一
 * Date: 2019/6/27
 * Description: LoginController
 */
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

//    /**
//     * 跳转到登录界面
//     *
//     * @param model
//     * @return
//     */
//    @RequestMapping("/toLogin")
//    public String toLogin(Model model) {
//        List<User> users = service.getAllUsers();
//        int userCount = users.size();
//        model.addAttribute("userCount", userCount);
//        return "user/login";
//    }


    @RequestMapping("/login")

    public Result login(@RequestParam("emailAddr") String emailAddr,
                        @RequestParam("password") String password,
                        HttpServletRequest request) throws IOException {

        Result result = loginService.login(emailAddr, password);
        if (result.getCode().equals(0)) {
//            HttpSession session = request.getSession();
//            session.setAttribute("emailAddr", emailAddr);
//            session.setAttribute("password", password);
            request.getSession().setAttribute("emailAddr", emailAddr);
            System.out.println(request.getSession().getId());
        }
        return result;
    }
}
