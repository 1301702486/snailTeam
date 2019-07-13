package com.snail.child.controller.user;

import com.snail.child.model.Result;
import com.snail.child.service.user.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2019/7/6
 * Description: No Description
 */

@RestController
public class ContactUsController {
    @Autowired
    ContactUsService contactUsService;

    /**
     * 联系我们
     *
     * @param name       用户名称
     * @param emailAddr  用户邮件联系方式
     * @param contentStr 邮件内容
     * @param title      邮件主题
     * @return 成功: code=0
     */
    @PostMapping("/contactUS")
    public Result contactUs(String name, String emailAddr, String contentStr, String title) {
        return contactUsService.contactUs(emailAddr, name, contentStr, title);
    }

}
