package com.snail.child.controller.releaseInfo;

import com.snail.child.model.ParentFindChild;
import com.snail.child.model.Result;
import com.snail.child.service.releaseInfo.ParentFindChildService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @ResponseBody
    @RequestMapping(value = "/session")
    public Map<String, Object> getSession(HttpServletRequest request) {
        request.getSession().setAttribute("userName", "1234");
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(HttpServletRequest request) {
        String userName = (String) request.getSession().getAttribute("userName");
        return userName;
    }


    @ApiOperation(value = "发布家长找孩子的信息")
    @PostMapping(value="/addParentFindChild")
    public Result addParentFindChild(HttpServletRequest request,ParentFindChild parentFindChild,
                                     @RequestParam("emailAddr") String emailAddr,
                                     @RequestParam("releasePhoto") MultipartFile file) {
        HttpSession session=request.getSession();
        String username=(String)request.getSession().getAttribute("emailAddr");
        return parentFindChildService.addParentFindChild(request,parentFindChild,emailAddr,file);
    }

    @ApiOperation(value="更新家长找孩子信息" )
    @PutMapping(value="/updateParentFindChild")
    public Result addParentFindChild(ParentFindChild parentFindChild){
        return parentFindChildService.updateParentFindChild(parentFindChild);
    }

    @ApiOperation(value="查询家长找孩子信息")
    @GetMapping(value="/selectParentFindChild")
    public Result selectParentFindChild(ParentFindChild parentFindChild, Pageable page){
        return parentFindChildService.selectParentFindChild(parentFindChild,page);
    }


    @ApiOperation("删除发布信息")
    @PostMapping(value="/deleteParentFindChild")
    public Result deleteParentFindChild(Integer id){
        return parentFindChildService.deleteParentFindChild(id);
    }
}
