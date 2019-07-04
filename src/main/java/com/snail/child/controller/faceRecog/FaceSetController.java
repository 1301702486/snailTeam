package com.snail.child.controller.faceRecog;

import com.snail.child.service.faceRecog.FaceSetService;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2019/7/3
 * Description: No Description
 */
@RestController
public class FaceSetController {
    @Autowired
    FaceSetService faceSetService;

    @PostMapping("/createFaceSet")
    public void createFaceSet() {
        String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
        // 创建参数队列
        List<BasicNameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("api_key", "GTvcIl8x4HX25ytluaDi7eicC7rv2Ad_"));
        formparams.add(new BasicNameValuePair("api_secret", "7CMHtPRjOD14UyEft6yb_-EJfDVaJjnj"));
        formparams.add(new BasicNameValuePair("outer_id", "myFaceSet_1"));
        // 发送请求
        faceSetService.post(formparams, url);
    }
}
