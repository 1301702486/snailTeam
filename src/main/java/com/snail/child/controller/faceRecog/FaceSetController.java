package com.snail.child.controller.faceRecog;

import com.snail.child.service.faceRecog.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2019/7/3
 * Description: No Description
 */
@RestController
public class FaceSetController {
    @Autowired
    FaceService faceService;

    @PostMapping("/createFaceSet")
    public String createFaceSet(String outerId) {
        return faceService.createFaceSet(outerId);
    }

    @PostMapping("/addToFaceSet")
    public String addToFaceSet(String faceTokens, String outerId) {
        return faceService.addToFaceSet(faceTokens, outerId);
    }

    @PostMapping("/deleteFaceSet")
    public String deleteAFaceSet(Integer checkEmpty, String outerId) {
        return faceService.deleteAFaceSet(checkEmpty, outerId);
    }

    @PostMapping("/removeFromFaceSet")
    public String removeFromFaceSet(String faceTokens, String outerId) {
        return faceService.removeFromFaceSet(faceTokens, outerId);
    }
}
