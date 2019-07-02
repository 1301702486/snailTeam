package com.snail.child.controller.releaseInfo;


import com.snail.child.model.ChildFindParent;
import com.snail.child.model.Result;
import com.snail.child.service.releaseInfo.ChildFindParentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: ZhangXinrui
 * Date: 2019/6/30
 * Description: No Description
 */
@RestController
public class ChildFindParentController {

    @Autowired
    ChildFindParentService childFindParentService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
         DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
         binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @ApiOperation(value="发布孩子找家长信息")
    @PostMapping("/addChildFindParent")
    public Result addChildFindParent(ChildFindParent childFindParent,
                                     @RequestParam("emailAddr") String emailAddr,
                                     @RequestParam( "releasePhoto") MultipartFile file){

               return childFindParentService.addChildFindParent(childFindParent,emailAddr,file);
    }

    @ApiOperation(value = "删除孩子找家长信息")
    @DeleteMapping("/deleteChildFindParent")
    public Result deleteChildFindParent(@RequestParam("id") Integer id){
        return childFindParentService.deleteChildFindParent(id);
    }

    @ApiOperation(value="查询孩子找父母的信息")
    @GetMapping(value="/selectChildFindParent")
    public Result selectChildFindParent(ChildFindParent childFindParent, Pageable pageable){
        return childFindParentService.selectChildFindParent(childFindParent,pageable);
    }

    @ApiOperation(value = "更新孩子找父母的信息")
    @PutMapping(value = "/updateChildFindParent")
    public Result updateChildFindParent(ChildFindParent childFindParent,
                                        @RequestParam("releasePhoto") MultipartFile file){
        return childFindParentService.updateChildFindParent(childFindParent,file);
    }
}