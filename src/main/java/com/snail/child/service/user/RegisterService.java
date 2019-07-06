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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * 注册
     *
     * @param emailAddr
     * @param password
     * @return
     */
    public Result register(String emailAddr, String password) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user != null)
            return ResultUtils.send(MessageChen.USER_EXIST);
        user = new User();
        user.setEmailAddr(emailAddr);
        user.setPassword(password);
        userRepository.save(user);
        EmailUtils sendEmail = new EmailUtils();
        String url = Url.baseUrl + Url.webMapping + "/" + "index.html";
        String content = "<h1>请点击链接完成注册。</h1>" +
                "<a href=\"" + url + "\">验证</a>";
        sendEmail.sendMessage(emailAddr, "【流浪宝贝】注册验证", content);
        return ResultUtils.send(MessageChen.GO_CONFIRM);
    }

    /**
     * 注册验证
     *
     * @param emailAddr
     * @param password
     * @return
     */
    public Result registerConfirm(String emailAddr, String password) {
        User user = new User();
        user.setEmailAddr(emailAddr);
        user.setPassword(password);
        userRepository.save(user);
        return ResultUtils.send(MessageChen.REGISTER_SUCCESS);
    }

    /**
     * 注册带头像的用户，以便测试“登录成功”
     *
     * @param emailAddr
     * @param password
     * @param file
     * @return
     */
    public Result testForLoginSuccess(String emailAddr, String password, MultipartFile file) {
        Result result = register(emailAddr, password);
        if (result != ResultUtils.send(MessageChen.USER_EXIST)) {
            User user = userRepository.findUserByEmailAddr(emailAddr);
            byte[] headPortrait = new byte[0];
            try {
                headPortrait = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            user.setHeadPortrait(headPortrait);
            userRepository.save(user);
            result = ResultUtils.send(MessageChen.GO_CONFIRM);
        }
        return result;
    }

//    /**
//     * 删除用户
//     *
//     * @param emailAddr
//     * @return
//     */
//    public Result deleteUser(String emailAddr){
//        User user = userRepository.findUserByEmailAddr(emailAddr);
//        userRepository.delete(user);
//        return ResultUtils.send(MessageChen.USER_DELETE);
//    }

}
