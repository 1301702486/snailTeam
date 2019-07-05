package com.snail.child.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * User: ZhangXinrui
 * Date: 2019/7/4
 * Description: No Description
 */

@Controller
public class mainController {

    @GetMapping("/")
    public String toMain() {
        return "index";
    }


}
