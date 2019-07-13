package com.snail.child.controller.releaseInfo;

import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.service.releaseInfo.ParentFindChildService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: ZhangXinrui
 * Date: 2019/6/29
 * Description: No Description
 */

@RestController
public class ParentFindChildController {

    @Autowired
    ParentFindChildService parentFindChildService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 发布家长找孩子的信息
     *
     * @param emailAddr       发布信息的用户id
     * @param parentFindChild 发布内容
     * @param file            上传的图片
     * @return 成功: code=0, data=匹配结果  失败: code!=0
     */
    @ApiOperation(value = "发布家长找孩子的信息")
    @PostMapping(value = "/addParentFindChild")
    public Result addParentFindChild(String emailAddr, ParentFindChild parentFindChild,
                                     @RequestParam("releasePhoto") MultipartFile file) {
        return parentFindChildService.addParentFindChild(parentFindChild, emailAddr, file);
    }

    /**
     * 查询家长找孩子信息
     *
     * @param parentFindChild 查询条件
     * @param pageNumber      页码
     * @param pageSize        页内数量
     * @return 查询结果
     */
    @ApiOperation(value = "查询家长找孩子信息")
    @GetMapping(value = "/selectParentFindChild")
    public Result selectParentFindChild(ParentFindChild parentFindChild, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return parentFindChildService.selectParentFindChild(parentFindChild, pageable);
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
    @GetMapping("/findChild/getMatchResult")
    public Result getPfcMatchResult(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        return parentFindChildService.getPfcMatchResult(id1, id2, id3, id4, id5);
    }

}
