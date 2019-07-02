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

    @RequestMapping("/test/cookie")
    public String cookie(@RequestParam("browser") String browser, HttpServletRequest request, HttpSession session) {
        //取出session中的browser
        Object sessionBrowser = session.getAttribute("browser");
        if (sessionBrowser == null) {
            System.out.println("不存在session，设置browser=" + browser);
            session.setAttribute("browser", browser);
        } else {
            System.out.println("存在session，browser=" + sessionBrowser.toString());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " : " + cookie.getValue());
            }
        }
        return "index";
    }


    @ApiOperation(value = "发布家长找孩子的信息")
    @PostMapping(value="/addParentFindChild")
    public Result addParentFindChild(ParentFindChild parentFindChild,
                                     @RequestParam("emailAddr") String emailAddr,
                                     @RequestParam("releasePhoto") MultipartFile file) {
        return parentFindChildService.addParentFindChild(parentFindChild,emailAddr,file);
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
