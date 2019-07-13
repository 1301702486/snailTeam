package com.snail.child.controller.faceRecog;

import com.snail.child.model.Result;
import com.snail.child.service.faceRecog.FaceSearchService;
import com.snail.child.service.faceRecog.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Author: 郭瑞景
 * Date: 2019/7/4
 * Description: No Description
 */

@RestController
public class FaceSearchController {
    @Autowired
    FaceService faceService;
    @Autowired
    FaceSearchService faceSearchService;

    @PostMapping("/faceSearch")
    public ArrayList<String> faceSearch(String targetFaceToken, String outerId) {
        return faceService.getFaceTokens(targetFaceToken, outerId);
    }

    /**
     * 根据发布id查找匹配结果
     *
     * @param id 发布id
     * @return 成功: code=211, data=宝贝寻家匹配结果; code=212, data=家寻宝贝匹配结果; code=213, data=疑似走失儿童匹配结果
     */
    @PostMapping("/searchFaceById")
    public Result searchFaceById(Integer id) {
        return faceSearchService.searchFaceById(id);
    }
}
