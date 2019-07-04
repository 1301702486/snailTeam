package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.UserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
 */

@RestController
public class UserUpdateController {

    @Autowired
    UserUpdateService userService;


    @PutMapping(value = "/updateUserInfo")
    public Result updateUserByEmailAddr(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String emailAddr = session.getAttribute("emailAddr").toString();
        Result result = userService.updateUserInfo(user, emailAddr);
        // TODO: 跳转到主页
        return result;
    }

    @PutMapping(value = "/changePassword")
    public Result changeUserPassword(@RequestParam(value = "emailAddr") String emailAddr,
                                     @RequestParam(value = "password") String password,
                                     HttpServletRequest request) {

        Result result = userService.changePassword(emailAddr, password);
        if (result.getCode().equals(0)) {
            HttpSession session = request.getSession();
            session.setAttribute("password", password);
        }
        // TODO: 跳转到主页
        return result;
    }

    /**
     * 注销
     *
     * @param request
     */
    @DeleteMapping(value = "/deleteUser")
    public Result deleteUserById(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String emailAddr = session.getAttribute("emailAddr").toString();
        Result result = userService.deleteUserById(emailAddr);
        if (result.getCode().equals(0)) {
            session.invalidate();
        }
        // TODO: 跳转到主页
        return result;
    }
}
