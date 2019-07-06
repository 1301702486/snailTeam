package com.snail.child.service.user;

import com.snail.child.enm.MessageGuo;
import com.snail.child.model.Address;
import com.snail.child.model.Result;
import com.snail.child.model.User;
import com.snail.child.repository.AddressRepository;
import com.snail.child.repository.UserRepository;
import com.snail.child.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No Description
 */

@Service
public class UserUpdateService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    /**
     * 添加一个用户
     *
     * @param user
     * @return
     */
    @Transactional
    public Result addUserInfo(User user) {
        if (user.getEmailAddr() != null && user.getPassword() != null && user.getAddress() != null) {
            return ResultUtils.send(MessageGuo.SUCCESS, userRepository.save(user));
        }
        return ResultUtils.send(MessageGuo.NULL_EADDR_AND_PWD_AND_ADDR);
    }


    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
//    @Transactional
//    public Result updateUserInfo(User user) {
//        if (user.getEmailAddr() != null) {
//            User user1 = findUserById(user.getEmailAddr());//user1是user的旧值
//            if (user.getPassword() == null) {
//                user.setPassword(user1.getPassword());
//            }
//            if (user.getBirthday() == null && user1.getBirthday() != null) {
//                user.setBirthday(user1.getBirthday());
//            }
//            if (user.getGender() == null && user1.getGender() != null) {
//                user.setGender(user1.getGender());
//            }
//            if (user.getPhone() == null && user1.getPhone() != null) {
//                user.setPhone(user1.getPhone());
//            }
//            if (user.getAddress() == null) {
//                if (user1.getAddress() != null) {
//                    user.setAddress(user1.getAddress());
//                }
//            } else {
//                if (user.getAddress().getProvince() == null) {
//                    if (user1.getAddress() != null) {
//                        user.getAddress().setProvince(user1.getAddress().getProvince());
//                    } else {
//                        return ResultUtils.send(MessageGuo.NULL_PROVINCE);
//                    }
//                }
//            }
//            Result result = ResultUtils.send(MessageGuo.SUCCESS, userRepository.save(user));
//            deleteAddresses();
//            return result;
//        }
//        return ResultUtils.send(MessageGuo.NO_EMAIL_ADDRESS);
//
//    }
    @Transactional
    public Result updateUserInfo(User user, String emailAddr) {
        User user1 = findUserById(emailAddr);   // user1是user的旧值
        if (user.getBirthday() == null && user1.getBirthday() != null) {
            user.setBirthday(user1.getBirthday());
        }
        if (user.getGender() == null && user1.getGender() != null) {
            user.setGender(user1.getGender());
        }
        if (user.getPhone() == null && user1.getPhone() != null) {
            user.setPhone(user1.getPhone());
        }
        if (user.getAddress() == null) {
            if (user1.getAddress() != null) {
                user.setAddress(user1.getAddress());
            }
        } else {
            if (user.getAddress().getProvince() == null) {
                if (user1.getAddress() != null) {
                    user.getAddress().setProvince(user1.getAddress().getProvince());
                } else {
                    return ResultUtils.send(MessageGuo.NULL_PROVINCE);
                }
            }
        }
        Result result = ResultUtils.send(MessageGuo.SUCCESS, userRepository.save(user));
        deleteAddresses();
        return result;
    }

    /**
     * 检查用户信息是否完善
     *
     * @param user
     * @return true: 完善; false: 不完善
     */
    public boolean infoComplete(User user) {
        boolean flag = true;
        if (user.getAddress() == null) {
            flag = false;
        } else if (user.getAddress().getProvince() == null) {
            flag = false;
        } else if (user.getAddress().getCity() == null) {
            flag = false;
        } else if (user.getAddress().getDistrict() == null) {
            flag = false;
        } else if (user.getBirthday() == null) {
            flag = false;
        } else if (user.getGender() == null) {
            flag = false;
        } else if (user.getPhone() == null) {
            flag = false;
        }
        return flag;
    }

    /**
     * 按用户名查询一个用户
     *
     * @param emailAddr
     * @return
     */
    public User findUserById(String emailAddr) {
        return userRepository.findUserByEmailAddr(emailAddr);
    }

    /**
     * 删除Address中不再被用户使用的地址
     */
    private void deleteAddresses() {
        List<User> users = userRepository.findAll();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < users.size(); ++i) {
            Integer addrId = users.get(i).getAddress().getId();
            ids.add(addrId);
        }
        List<Address> addresses = addressRepository.findAll();
        for (int j = 0; j < addresses.size(); ++j) {
            if (!ids.contains(addresses.get(j).getId())) {
                addressRepository.delete(addresses.get(j));
            }
        }
    }

    /**
     * 删除一个用户
     *
     * @param emailAddr
     */
    public Result deleteUserById(String emailAddr) {
        if (findUserById(emailAddr) != null) {
            userRepository.delete(findUserById(emailAddr));
            return ResultUtils.send(MessageGuo.SUCCESS, userRepository.findAll());
        } else {
            return ResultUtils.send(MessageGuo.NO_EMAIL_ADDRESS);
        }
    }

    /**
     * 修改密码
     *
     * @param emailAddr
     * @param password
     * @return
     */
    public Result changePassword(String emailAddr, String password) {
        if (emailAddr != null) {
            User user = findUserById(emailAddr);
            if (user != null && user.getPassword() != password) {
                user.setPassword(password);
                return ResultUtils.send(MessageGuo.SUCCESS, userRepository.save(user));
            }
        }
        return ResultUtils.send(MessageGuo.CHANGE_PWD_FAILED);
    }

}
