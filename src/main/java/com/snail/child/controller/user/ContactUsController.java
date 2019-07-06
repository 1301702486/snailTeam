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

    @PostMapping("/contactUS")
    public Result contactUs(String name, String emailAddr, String contentStr, String title) {
        return contactUsService.contactUs(emailAddr, name, contentStr, title);
    }

}
