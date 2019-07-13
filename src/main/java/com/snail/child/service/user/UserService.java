package com.snail.child.service.user;

import com.snail.child.enm.MessageGuo;
import com.snail.child.model.*;
import com.snail.child.repository.*;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No Description
 */

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ParentFindChildRepository pfcRepository;
    @Autowired
    ChildFindParentRepository cfpRepository;
    @Autowired
    SuspectedMissingChildRepository smcRepository;

    /**
     * 更新用户信息
     *
     * @param user      用户信息
     * @param emailAddr 用户id
     * @return 成功: code=0, data=user  失败: code!=0
     */
    @Transactional
    public Result updateUserInfo(User user, String emailAddr) {
        User user1 = findUserById(emailAddr);   // user1是user的旧值
        if (user.getBirthday() != null) {
            user1.setBirthday(user.getBirthday());
        }
        if (user.getGender() != null) {
            user1.setGender(user.getGender());
        }
        if (user.getPhone() != null) {
            user1.setPhone(user.getPhone());
        }
        if (user.getAddress() != null) {
            if (user.getAddress().getProvince() != null) {
                if (user1.getAddress() == null) {
                    user1.setAddress(user.getAddress());
                } else {
                    if (user.getAddress().getCity() != null) {
                        user1.getAddress().setCity(user.getAddress().getCity());
                    }
                    if (!user1.getAddress().getProvince().equals(user.getAddress().getProvince())) {
                        user1.getAddress().setProvince(user.getAddress().getProvince());
                    }
                    if (user.getAddress().getDistrict() != null) {
                        user1.getAddress().setDistrict(user.getAddress().getDistrict());
                    }
                    if (user.getAddress().getDetail() != null) {
                        user1.getAddress().setDetail(user.getAddress().getDetail());
                    }
                }
            } else {
                return ResultUtils.send(MessageGuo.NULL_PROVINCE);
            }
        }

        return ResultUtils.send(MessageGuo.SUCCESS, userRepository.save(user1));
    }

    /**
     * 按用户名查询一个用户
     *
     * @param emailAddr 用户名
     * @return 查找到的user, 可能为null
     */
    public User findUserById(String emailAddr) {
        return userRepository.findUserByEmailAddr(emailAddr);
    }


    /**
     * 修改密码
     *
     * @param emailAddr 用户id
     * @param password  新密码
     * @return 成功: code=0  失败: code!=0
     */
    public Result changePassword(String emailAddr, String password) {
        if (emailAddr != null) {
            User user = findUserById(emailAddr);
            if (user != null) {
                user.setPassword(password);
                userRepository.save(user);
                return ResultUtils.send(MessageGuo.SUCCESS);
            } else {
                return ResultUtils.send(MessageGuo.NO_USER);
            }
        }
        return ResultUtils.send(MessageGuo.CHANGE_PWD_FAILED);
    }

    /**
     * 获取用户发布的宝贝寻家信息
     *
     * @param emailAddr 用户id
     * @return 该用户发布的宝贝寻家信息
     */
    public Result getCfpReleaseList(String emailAddr) {
        User user = findUserById(emailAddr);
        if (user == null) {
            return ResultUtils.send(MessageGuo.NO_USER);
        }
        if (user.getChildFindParent() != null) {
            return ResultUtils.send(MessageGuo.CFP_RELEASE, user.getChildFindParent());
        }
        return ResultUtils.send(MessageGuo.NO_RELEASE);
    }

    /**
     * 获取用户发布的家寻宝贝信息
     *
     * @param emailAddr 用户id
     * @return 该用户发布的家寻宝贝信息
     */
    public Result getPfcReleaseList(String emailAddr) {
        User user = findUserById(emailAddr);
        if (user == null) {
            return ResultUtils.send(MessageGuo.NO_USER);
        }
        if (user.getParentFindChild() != null) {
            return ResultUtils.send(MessageGuo.PFC_RELEASE, user.getParentFindChild());
        }
        return ResultUtils.send(MessageGuo.NO_RELEASE);
    }

    /**
     * 获取用户发布的疑似走失儿童列表
     *
     * @param emailAddr 用户id
     * @return 该用户发布的疑似走失儿童列表
     */
    public Result getSmcReleaseList(String emailAddr) {
        User user = findUserById(emailAddr);
        if (user == null) {
            return ResultUtils.send(MessageGuo.NO_USER);
        }
        if (user.getSuspectedMissingChildren().size() > 0) {
            return ResultUtils.send(MessageGuo.SMC_RELEASE, user.getSuspectedMissingChildren());
        }
        return ResultUtils.send(MessageGuo.NO_RELEASE);
    }

    /**
     * 根据发布id获取发布信息
     *
     * @param id 发布id
     * @return 此条发布信息
     */
    public Result getReleaseById(Integer id) {
        ParentFindChild parentFindChild = pfcRepository.findParentFindChildById(id);
        ChildFindParent childFindParent = cfpRepository.findChildFindParentById(id);
        SuspectedMissingChild suspectedMissingChild = smcRepository.findSuspectedMissingChildById(id);
        if (parentFindChild != null) {
            return ResultUtils.send(MessageGuo.SUCCESS, parentFindChild);
        }
        if (childFindParent != null) {
            return ResultUtils.send(MessageGuo.SUCCESS, childFindParent);
        }
        if (suspectedMissingChild != null) {
            return ResultUtils.send(MessageGuo.SUCCESS, suspectedMissingChild);
        }
        return ResultUtils.send(MessageGuo.NO_RELEASE);
    }
}
