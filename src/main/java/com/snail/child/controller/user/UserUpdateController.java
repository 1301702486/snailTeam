package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.UserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
 */

@RestController
public class UserUpdateController {

    @Autowired
    UserUpdateService userService;


    @PostMapping(value = "/addUserInfo")
    public Result addUser(User user) {

        return userService.addUserInfo(user);
    }



    @PutMapping(value = "/updateUserInfo")
    public Result updateUserByEmailAddr(User user) {

        return userService.updateUserInfo(user);
    }

    @PutMapping(value = "/changePassword")
    public Result changeUserPassword(@RequestParam(value = "emailAddr") String emailAddr,
                                     @RequestParam(value = "password") String password) {

        return userService.changePassword(emailAddr, password);
    }

    /**
     * 删除一个用户
     *
     * @param emailAddr
     */
    @DeleteMapping(value = "/deleteUser")
    public Result deleteUserById(@RequestParam(value = "emailAddr") String emailAddr) {
        return userService.deleteUserById(emailAddr);
    }
}
