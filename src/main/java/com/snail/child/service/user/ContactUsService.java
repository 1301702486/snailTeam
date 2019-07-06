package com.snail.child.service.user;

import com.snail.child.enm.MessageGuo;
import com.snail.child.model.Result;
import com.snail.child.utils.EmailUtils;
import com.snail.child.utils.ResultUtils;
import org.springframework.stereotype.Service;

/**
 * Author: 郭瑞景
 * Date: 2019/7/6
 * Description: No Description
 */

@Service
public class ContactUsService {
    public Result contactUs(String emailAddr, String name, String contentStr, String title) {
        EmailUtils emailUtil = new EmailUtils();
        String content = "来自：" + name + "<br>" + contentStr + "<br>" + "联系邮箱：" + emailAddr;
        emailUtil.sendMessage("1301702486@qq.com", "【流浪宝贝】" + title, content);
        return ResultUtils.send(MessageGuo.SUCCESS);
    }
}
