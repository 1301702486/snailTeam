package com.snail.child.controller;

import com.snail.child.model.Result;
import com.snail.child.res.Url;
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
        String url = Url.baseUrl + Url.webMapping + "/index.html";
        response.sendRedirect(url);
    }

    @RequestMapping("/index")
    public void toIndex(HttpServletResponse response) throws IOException {
        String url = Url.baseUrl + Url.webMapping + "/index.html";
        response.sendRedirect(url);
    }

}
