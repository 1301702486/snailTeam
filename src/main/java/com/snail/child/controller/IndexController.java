package com.snail.child.controller;

import com.snail.child.model.Result;
import com.snail.child.res.Url;
import com.snail.child.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    IndexService indexService;

    /**
     * 重定向到主页
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/")
    public void defaultToIndex(HttpServletResponse response) throws IOException {
        String url = Url.baseUrl + Url.webMapping + "/index.html";
        response.sendRedirect(url);
    }

    /**
     * 重定向到主页
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/index")
    public void toIndex(HttpServletResponse response) throws IOException {
        String url = Url.baseUrl + Url.webMapping + "/index.html";
        response.sendRedirect(url);
    }

    /**
     * 查询最新走失信息
     *
     * @param province 走失省份
     * @param pageNum  页码
     * @param size     每页显示数量
     * @return 成功: code=0, data=[总页数, 每页内容]
     */
    @GetMapping("/selectLatestChild")
    public Result selectLatestChild(String province, int pageNum, int size) {
        return indexService.selectLatestChild(province, pageNum, size);
    }

    /**
     * 联系发布人
     *
     * @param emailAddr  用户的联系邮箱
     * @param id         发布id
     * @param name       用户名称
     * @param contentStr 邮件内容
     * @param title      主题
     * @return 成功 code=0,失败:code=408, 提示发布用户不存在
     * @author 郭瑞景
     */
    @PostMapping("/contactUser")
    public Result contactUser(String emailAddr, Integer id, String name, String contentStr, String title) {
        return indexService.contactUser(emailAddr, id, name, contentStr, title);
    }

}
