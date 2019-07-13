package com.snail.child.service.user;


import com.snail.child.enm.MessageChen;
import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.repository.UserRepository;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: 陈一
 * Date: 2019/6/28
 * Description: LoginService
 */
@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 登录
     *
     * @param emailAddr 用户邮箱地址
     * @param password  密码
     * @return 成功: code=0
     */
    public Result login(String emailAddr, String password) {
        User user = userRepository.findUserByEmailAddr(emailAddr);
        if (user == null)
            return ResultUtils.send(MessageChen.PASSWORD_WRONG);
        String rightPassword = user.getPassword();
        if (password.equals(rightPassword))
            return ResultUtils.send(MessageChen.LOGIN_SUCCESS);
        else
            return ResultUtils.send(MessageChen.PASSWORD_WRONG);
    }

}
