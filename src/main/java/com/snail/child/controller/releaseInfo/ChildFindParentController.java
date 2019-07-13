package com.snail.child.controller.releaseInfo;


import com.snail.child.enm.MessageXin;
import com.snail.child.model.ChildFindParent;
import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.service.releaseInfo.ChildFindParentService;
import com.snail.child.utils.ResultUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 发布宝贝寻家信息
     *
     * @param childFindParent 接收前端传过来的发布信息
     * @param emailAddr       用户id
     * @param file            用户上传的图片
     * @return 成功: code=0, data=匹配结果  失败: code!=0
     */
    @ApiOperation(value = "发布孩子找家长信息")
    @PostMapping("/addChildFindParent")
    public Result addChildFindParent(ChildFindParent childFindParent,
                                     String emailAddr,
                                     @RequestParam("releasePhoto") MultipartFile file) {
        return childFindParentService.addChildFindParent(childFindParent, emailAddr, file);
    }

    /**
     * 查询宝贝寻家信息
     *
     * @param childFindParent 查询条件
     * @param pageNumber      页码
     * @param pageSize        页内数量
     * @return 查询结果
     */
    @ApiOperation(value = "查询孩子找父母的信息")
    @GetMapping(value = "/selectChildFindParent")
    public Result selectChildFindParent(ChildFindParent childFindParent, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return childFindParentService.selectChildFindParent(childFindParent, pageable);
    }

    /**
     * 根据匹配结果的发布id查找对应的发布信息
     * id的值由前端传递过来
     * 最多有5个匹配结果, 少于5个其余id值为-1
     *
     * @param id1
     * @param id2
     * @param id3
     * @param id4
     * @param id5
     * @return 根据id查到的发布信息
     */
    @GetMapping("/findFamily/getMatchResult")
    public Result getCfpMatchResult(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        return childFindParentService.getCfpMatchResult(id1, id2, id3, id4, id5);
    }

}