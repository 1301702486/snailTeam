package com.snail.child.controller.releaseInfo;

import com.snail.child.enm.MessageXin;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.utils.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * User: ZhangXinrui
 * Date: 2019/6/29
 * Description: No Description
 */
@RestController
public class UploadController {

    @PostMapping("/uploadFile")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            return ResultUtils.send(MessageXin.PARENTFINDCHILD_HAS_EXIST);
        }
        try {
            ParentFindChild fc = new ParentFindChild();
            fc.setPhoto(file.getBytes());
            if(fc.getPhoto().length == 0){
               return ResultUtils.send(MessageXin.PARENTFINDCHILD_HAS_EXIST);
            }
            return ResultUtils.send(MessageXin.SUCCESS);
        } catch (IOException e) {
            return ResultUtils.send(MessageXin.PARENTFINDCHILD_NOT_EXIST);
        }
    }

}
