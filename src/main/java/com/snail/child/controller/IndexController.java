package com.snail.child.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 陈一
 * Date: 2019/6/30
 * Description: No Description
 */
@RestController
public class IndexController {

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/")
    public String toIndex() {
        return "redirect:/index";
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
