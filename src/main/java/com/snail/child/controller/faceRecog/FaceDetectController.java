package com.snail.child.controller.faceRecog;

import com.snail.child.service.faceRecog.FaceDetectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Author: 郭瑞景
 * Date: 2019/7/3
 * Description: No Description
 */

@RestController
public class FaceDetectController {
    @Autowired
    FaceDetectService faceDetectService;

    @PostMapping(value = "/face")
    public String getFaceToken(String imgUrl) {
        return faceDetectService.getFaceToken(imgUrl);
    }
}
