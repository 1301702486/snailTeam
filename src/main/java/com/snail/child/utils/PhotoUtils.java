package com.snail.child.utils;

import com.snail.child.res.Url;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * User: ZhangXinrui
 * Date: 2019/7/4
 * Description: No Description
 */
public class PhotoUtils {
    private static String uploadDir = "C:/images/";

    public static String uploadPhoto(MultipartFile file) {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        System.out.println("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = uploadDir;
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            System.out.println("上传成功后的文件路径为：" + filePath + fileName);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = Url.baseUrl + Url.imageMapping + "/";
        return path + fileName;
    }

}
