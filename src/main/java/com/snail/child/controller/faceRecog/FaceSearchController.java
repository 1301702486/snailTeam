package com.snail.child.controller.faceRecog;

import com.snail.child.service.faceRecog.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2019/7/4
 * Description: No Description
 */

@RestController
public class FaceSearchController {
    @Autowired
    FaceService faceService;

    @PostMapping("/faceSearch")
    public ArrayList<String> faceSearch(String targetFaceToken, String outerId) {
        return faceService.getfaceTokens(targetFaceToken, outerId);
    }
}
