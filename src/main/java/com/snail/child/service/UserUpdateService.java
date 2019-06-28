package com.snail.child.service;

import com.snail.child.enm.MsgId;
import com.snail.child.entity.Address;
import com.snail.child.entity.Result;
import com.snail.child.entity.User;
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
            return ResultUtils.success(userRepository.save(user));
        }
        return ResultUtils.error(MsgId.NULL_EADDR_AND_PWD_AND_ADDR);
    }


    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Transactional
    public Result updateUserInfo(User user) {
        if (user.getEmailAddr() != null) {
            User user1 = findUserById(user.getEmailAddr());//user1是user的旧值
            if (user.getPassword() == null) {
                user.setPassword(user1.getPassword());
            }
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
                        return ResultUtils.error(MsgId.NULL_PROVINCE);
                    }
                }
            }
            Result result = ResultUtils.success(userRepository.save(user));
            deleteAddresses();
            return result;
        }
        return ResultUtils.error(MsgId.NO_EMAIL_ADDRESS);

    }

    /*
    public User updateUserById(String emailAddr, String pwd, String gender, String phone, Date date) {

        User user = findUserById(emailAddr);
        if (user != null) {
            if (pwd != null && pwd != user.getPassword()) {
                user.setPassword(pwd);
            }
            if (gender != null && gender != user.getGender()) {
                user.setGender(gender);
            }
            if (phone != null && phone != user.getPhone()) {
                user.setPhone(phone);
            }
            if (date != null && date != user.getBirthday()) {
                user.setBirthday(date);
            }

            return userRepository.save(user);
        } else {
            // TODO 用户不存在
            return null;
        }
    }
    */

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
            return ResultUtils.success(userRepository.findAll());
        }
        else {
            return ResultUtils.error(MsgId.NO_EMAIL_ADDRESS);
        }
    }

    /*
    public void updateUserInfo(String emailAddr, String pwd, String gender, String phone,
                               Date date, String province, String city, String county,
                               String district, String street, String detail) {

        User user = updateUserById(emailAddr, pwd, gender, phone, date);
        Address addr = user.getAddress();
        if (addr == null && !(city == null && county == null && district == null && street == null && detail == null && province == null)) {
            if (province != null) {
                addr = new Address();
                addr.setProvince(province);
                if (city != null) {
                    addr.setCity(city);
                }
                if (county != null) {
                    addr.setCounty(county);
                }
                if (district != null) {
                    addr.setDistrict(district);
                }
                if (street != null) {
                    addr.setStreet(street);
                }
                if (detail != null) {
                    addr.setDetail(detail);
                }
                addressRepository.save(addr);
                user.setAddress(addr);
                userRepository.save(user);
            } else {
                // TODO 一开始未填写地址且更新个人信息时更新了地址信息但未填写省份, 报错
                return;
            }
        } else if (addr != null) {
            if (province != null && province != addr.getProvince()) {
                addr.setProvince(province);
            }
            if (city != null && city != addr.getCity()) {
                addr.setCity(city);
            }
            if (county != null && county != addr.getCounty()) {
                addr.setCounty(county);
            }
            if (district != null && district != addr.getDistrict()) {
                addr.setDistrict(district);
            }
            if (street != null && street != addr.getStreet()) {
                addr.setStreet(street);
            }
            if (detail != null && detail != addr.getDetail()) {
                addr.setDetail(detail);
            }
            addressRepository.save(addr);
        }
    }
    */
}
