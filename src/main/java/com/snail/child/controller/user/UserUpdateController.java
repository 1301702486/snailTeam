package com.snail.child.controller.user;

import com.snail.child.enm.MessageGuo;
import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.UserUpdateService;
import com.snail.child.utils.ResultUtils;
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
    public Result updateUserByEmailAddr(User user, String emailAddr) {
        return userService.updateUserInfo(user, emailAddr);
    }

    @PutMapping(value = "/changePassword")
    public Result changeUserPassword(@RequestParam(value = "emailAddr") String emailAddr,
                                     @RequestParam(value = "password") String password) {

        return userService.changePassword(emailAddr, password);
    }

    /**
     * 注销
     *
     * @param emailAddr
     */
    @DeleteMapping(value = "/deleteUser")
    public Result deleteUserById(String emailAddr) {
        return userService.deleteUserById(emailAddr);
    }

    @GetMapping("/getUserInfo")
    public Result getUserInfo(String emailAddr) {
        User user = userService.findUserById(emailAddr);
        if (user != null) {
            return ResultUtils.send(MessageGuo.SUCCESS, user);
        } else {
            return ResultUtils.send(MessageGuo.NO_USER);
        }
    }
}
