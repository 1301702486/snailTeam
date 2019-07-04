package com.snail.child.controller.releaseInfo;

import com.snail.child.model.Result;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.service.releaseInfo.SuspectedMissingChildService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * User: ZhangXinrui
 * Date: 2019/6/30
 * Description: No Description
 */

@RestController
public class SuspectedMissingChildController {

    @Autowired
    SuspectedMissingChildService suspectedMissingChildService;

    @ApiOperation("添加疑似流浪儿童信息")
    @PostMapping(value = "/addSuspectedMissingChild")
    public Result addSuspectedMissingChild(HttpServletRequest request,
                                           SuspectedMissingChild suspectedMissingChild,
                                           @RequestParam("releasePhoto") MultipartFile file){
        String emailAddr=request.getSession().getAttribute("emailAddr").toString();
        return suspectedMissingChildService.addSuspectedMissingChild(suspectedMissingChild,emailAddr,file);
    }

    @ApiOperation("删除疑似流浪儿童信息")
    @DeleteMapping("deleteSuspectedMissingChild")
    public Result deleteSuspectedMissingChild(Integer id,HttpServletRequest request){
        String emailAddr=request.getSession().getAttribute("emailAddr").toString();
        return suspectedMissingChildService.deleteSuspectedMissingParent(id,emailAddr);
    }

    @ApiOperation("查询疑似流浪儿童信息")
    @GetMapping("/selectSuspectedMissingChild")
    public Result selectSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild, Pageable pageable){
        return suspectedMissingChildService.selectSuspectedMissingChild(suspectedMissingChild,pageable);
    }
}
