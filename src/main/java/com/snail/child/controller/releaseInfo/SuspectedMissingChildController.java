package com.snail.child.controller.releaseInfo;

import com.snail.child.model.Result;
import com.snail.child.model.SuspectedMissingChild;
import com.snail.child.service.releaseInfo.SuspectedMissingChildService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: ZhangXinrui
 * Date: 2019/6/30
 * Description: No Description
 */

@RestController
public class SuspectedMissingChildController {

    @Autowired
    SuspectedMissingChildService suspectedMissingChildService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 添加疑似流浪儿童信息
     *
     * @param emailAddr             用户id
     * @param suspectedMissingChild 接收发布信息
     * @param file                  用户上传的图片
     * @return 成功: code=0, data=匹配结果  失败: code!=0
     */
    @ApiOperation("添加疑似流浪儿童信息")
    @PostMapping(value = "/addSuspectedMissingChild")
    public Result addSuspectedMissingChild(String emailAddr, SuspectedMissingChild suspectedMissingChild,
                                           @RequestParam("releasePhoto") MultipartFile file) {
        return suspectedMissingChildService.addSuspectedMissingChild(suspectedMissingChild, emailAddr, file);
    }

    /**
     * 查询疑似流浪儿童信息
     *
     * @param suspectedMissingChild 查询条件
     * @param pageNumber            页码
     * @param pageSize              页内数量
     * @return 查询结果
     */
    @ApiOperation("查询疑似流浪儿童信息")
    @GetMapping("/selectSuspectedMissingChild")
    public Result selectSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return suspectedMissingChildService.selectSuspectedMissingChild(suspectedMissingChild, pageable);
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
    @GetMapping("/lostChild/getMatchResult")
    public Result getSmcMatchResult(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        return suspectedMissingChildService.getSmcMatchResult(id1, id2, id3, id4, id5);
    }
}
