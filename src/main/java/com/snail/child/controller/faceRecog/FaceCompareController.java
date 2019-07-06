//package com.snail.child.controller.faceRecog;
//
//import com.snail.child.service.faceRecog.FaceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Author: 郭瑞景
// * Date: 2019/7/4
// * Description: No Description
// */
//
//@RestController
//public class FaceCompareController {
//    @Autowired
//    FaceService faceService;
//
//    @PostMapping("/faceCompare")
//    public String faceCompare(String faceToken1, String faceToken2) {
//        return faceService.faceCompare(faceToken1, faceToken2);
//    }
//}
