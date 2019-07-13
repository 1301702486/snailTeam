package com.snail.child.controller.user;

import com.snail.child.enm.MessageGuo;
import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.service.user.UserService;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
 */

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 更新用户信息
     *
     * @param user      用户信息
     * @param emailAddr 用户id
     * @return 成功: code=0, data=user  失败: code!=0
     */
    @PutMapping(value = "/updateUserInfo")
    public Result updateUserByEmailAddr(User user, String emailAddr) {
        return userService.updateUserInfo(user, emailAddr);
    }

    /**
     * 修改密码
     *
     * @param emailAddr 用户id
     * @param password  新密码
     * @return 成功: code=0  失败: code!=0
     */
    @PutMapping(value = "/changePassword")
    public Result changeUserPassword(@RequestParam(value = "emailAddr") String emailAddr,
                                     @RequestParam(value = "password") String password) {

        return userService.changePassword(emailAddr, password);
    }

    /**
     * 获取用户信息
     *
     * @param emailAddr 用户id
     * @return 成功: code=0, data=user  失败: code!=0
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo(String emailAddr) {
        User user = userService.findUserById(emailAddr);
        if (user != null) {
            return ResultUtils.send(MessageGuo.SUCCESS, user);
        } else {
            return ResultUtils.send(MessageGuo.NO_USER);
        }
    }

    /**
     * 获取用户发布的宝贝寻家信息
     *
     * @param emailAddr 用户id
     * @return 该用户发布的宝贝寻家信息
     */
    @GetMapping("/getCfpRelease")
    public Result getCfpReleaseList(String emailAddr) {
        return userService.getCfpReleaseList(emailAddr);
    }

    /**
     * 获取用户发布的家寻宝贝信息
     *
     * @param emailAddr 用户id
     * @return 该用户发布的家寻宝贝信息
     */
    @GetMapping("/getPfcRelease")
    public Result gePfcReleaseList(String emailAddr) {
        return userService.getPfcReleaseList(emailAddr);
    }

    /**
     * 获取用户发布的疑似走失儿童列表
     *
     * @param emailAddr 用户id
     * @return 该用户发布的疑似走失儿童列表
     */
    @GetMapping("/getSmcRelease")
    public Result getSmcReleaseList(String emailAddr) {
        return userService.getSmcReleaseList(emailAddr);
    }

    /**
     * 根据发布id获取发布信息
     *
     * @param id 发布id
     * @return 此条发布信息
     */
    @GetMapping("/getReleaseById")
    public Result getReleaseById(Integer id) {
        return userService.getReleaseById(id);
    }
}
