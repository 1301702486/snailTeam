package com.snail.child.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: 郭瑞景
 * Date: 2019/7/5
 * Description: No Description
 */
@RestController
public class IndexController {
    @RequestMapping("/")
    public void defaultToIndex(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://120.55.164.189:8081/index.html");
    }

    @RequestMapping("/index")
    public void toIndex(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://120.55.164.189:8081/index.html");
    }
}
