package com.snail.child.service.faceRecog;

import com.snail.child.enm.MessageGuo;
import com.snail.child.enm.MessageXin;
import com.snail.child.model.ChildFindParent;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.service.releaseInfo.ChildFindParentService;
import com.snail.child.service.releaseInfo.ParentFindChildService;
import com.snail.child.service.releaseInfo.SuspectedMissingChildService;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Author: 郭瑞景
 * Date: 2019/7/10
 * Description: No Description
 */

@Service
public class FaceSearchService {
    @Autowired
    ChildFindParentService cfpService;
    @Autowired
    ParentFindChildService pfcService;
    @Autowired
    SuspectedMissingChildService smcService;

    /**
     * 根据发布id查找匹配结果
     *
     * @param id 发布id
     * @return 成功: code=211, data=宝贝寻家匹配结果; code=212, data=家寻宝贝匹配结果; code=213, data=疑似走失儿童匹配结果
     */
    public Result searchFaceById(Integer id) {
        String targetFaceToken;
        ChildFindParent cfpRelease = cfpService.getCfpById(id);
        ParentFindChild pfcRelease = pfcService.getPfcById(id);
        SuspectedMissingChild smcRelease = smcService.getSmcById(id);
        if (cfpRelease != null) {
            targetFaceToken = cfpRelease.getFaceToken();
            ArrayList<ParentFindChild> results = cfpService.getMatchResults(targetFaceToken);
            return ResultUtils.send(MessageGuo.CFP_MATCH_RESULT, results);
        } else if (pfcRelease != null) {
            targetFaceToken = pfcRelease.getFaceToken();
            ArrayList<Object> results1 = pfcService.getMatchResults(targetFaceToken);
            return ResultUtils.send(MessageGuo.PFC_MATCH_RESULT, results1);
        } else if (smcRelease != null) {
            targetFaceToken = smcRelease.getFaceToken();
            ArrayList<ParentFindChild> results2 = smcService.getMatchResults(targetFaceToken);
            return ResultUtils.send(MessageGuo.SMC_MATCH_RESULT, results2);
        } else {
            return ResultUtils.send(MessageXin.NO_MATCH_RESULT);
        }
    }
}
