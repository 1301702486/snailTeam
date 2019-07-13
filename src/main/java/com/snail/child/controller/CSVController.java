package com.snail.child.controller;

import com.snail.child.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: 郭瑞景
 * Date: 2019/7/7
 * Description: No Description
 */

@RestController
public class CSVController {
    @Autowired
    CSVService csvService;

    /**
     * 把爬下来的信息添加到家寻宝贝数据库
     *
     * @param filePath csv文件路径
     */
    @PostMapping("/loadToPFC")
    public void loadToPFC(String filePath) {
        csvService.insertToPFC(filePath);
    }

    /**
     * 把爬下来的信息添加到宝贝寻家数据库
     *
     * @param filePath csv文件路径
     */
    @PostMapping("/loadToCFP")
    public void loadToCFP(String filePath) {
        csvService.insertToCFP(filePath);
    }
}
