package com.team.snail.child.controller;

import com.team.snail.child.service.UserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
 */

@RestController
public class UserUpdateController {

    @Autowired
    UserUpdateService userService;


    @PostMapping(value = "/addUserInfo")
    public void addUser(@RequestParam("emailAddr") String emailAddr,
                        @RequestParam("password") String pwd,
                        @RequestParam(value = "gender", required = false) String gender,
                        @RequestParam(value = "phone", required = false) String phone,
                        @RequestParam(value = "birthday", required = false) Date date,
                        @RequestParam(value = "province", required = false) String province,
                        @RequestParam(value = "city", required = false) String city,
                        @RequestParam(value = "county", required = false) String county,
                        @RequestParam(value = "district", required = false) String district,
                        @RequestParam(value = "street", required = false) String street,
                        @RequestParam(value = "detail", required = false) String detail) {

        userService.addUserInfo(emailAddr, pwd, gender, phone, date, province, city, county, district, street, detail);
    }

    /**
     * 更新用户信息(包括地址)
     *
     * @param emailAddr
     * @param pwd
     * @param gender
     * @param phone
     * @param date
     * @param province
     * @param city
     * @param county
     * @param district
     * @param street
     * @param detail
     */
    @PutMapping(value = "/updateUserInfo")
    public void updateUserByEmailAddr(@RequestParam("emailAddr") String emailAddr,
                                      @RequestParam(value = "password", required = false) String pwd,
                                      @RequestParam(value = "gender", required = false) String gender,
                                      @RequestParam(value = "phone", required = false) String phone,
                                      @RequestParam(value = "birthday", required = false) Date date,
                                      @RequestParam(value = "province", required = false) String province,
                                      @RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "county", required = false) String county,
                                      @RequestParam(value = "district", required = false) String district,
                                      @RequestParam(value = "street", required = false) String street,
                                      @RequestParam(value = "detail", required = false) String detail) {

        userService.updateUserInfo(emailAddr, pwd, gender, phone, date, province, city, county, district, street, detail);
    }

    /**
     * 删除一个用户
     *
     * @param emailAddr
     */
    @DeleteMapping(value = "/deleteUser")
    public void deleteUserById(@RequestParam(value = "emailAddr") String emailAddr) {
        userService.deleteUserById(emailAddr);
    }
}
