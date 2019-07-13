package com.snail.child.service.user;

import com.snail.child.enm.MessageChen;
import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.repository.UserRepository;
import com.snail.child.res.Url;
import com.snail.child.utils.EmailUtils;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

/**
 * Author: 陈一
 * Date: 2019/6/28
 * Description: RegisterService
 */
@Service
public class RegisterService {

    @Autowired
    UserRepository userRepository;

    /**
     * 发送验证链接
     *
     * @param emailAddr 注册邮箱地址
     * @param password  密码
     * @return 成功: code=0
     * @author 郭瑞景
     */
    public Result sendConfirmLink(String emailAddr, String password) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user != null)
            return ResultUtils.send(MessageChen.USER_EXIST);
        EmailUtils sendEmail = new EmailUtils();
        String url = Url.baseUrl + Url.webMapping + "/" + "registerHandler.html?emailAddr=" + emailAddr + "&password=" + password;
        String content = "<h1>请点击链接完成注册。</h1>" +
                "<a href='" + url + "'>验证</a>";
        sendEmail.sendMessage(emailAddr, "【流浪宝贝】注册验证", content);
        return ResultUtils.send(MessageChen.GO_CONFIRM);
    }

    /**
     * 发送邮件验证码
     *
     * @param emailAddr 邮箱地址
     * @return 成功: code=0
     * @author 郭瑞景
     */
    public Result sendCode(String emailAddr) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user != null)
            return ResultUtils.send(MessageChen.USER_EXIST);
        EmailUtils sendEmail = new EmailUtils();
        Random random = new Random();
        Integer randomCode = random.nextInt(9999) % 9000 + 1000;
        String content = "<h1></h1>" + "<h3>" + randomCode + "</h3>";
        sendEmail.sendMessage(emailAddr, "【流浪宝贝】", content);
        return ResultUtils.send(MessageChen.GO_CONFIRM, randomCode);
    }

    /**
     * 注册
     *
     * @param emailAddr 邮箱地址
     * @param password  密码
     * @return 成功: code=0
     * @author 郭瑞景
     */
    public Result register(String emailAddr, String password) {
        User user = new User();
        user.setEmailAddr(emailAddr);
        user.setPassword(password);
        userRepository.save(user);
        return ResultUtils.send(MessageChen.REGISTER_SUCCESS);
    }

}
