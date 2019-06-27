package com.team.snail.child.service;

import com.team.snail.child.entity.Address;
import com.team.snail.child.entity.User;
import com.team.snail.child.repository.AddressRepository;
import com.team.snail.child.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
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
    public void addUserInfo(String emailAddr, String pwd, String gender, String phone,
                            Date date, String province, String city, String county,
                            String district, String street, String detail) {

        User user = new User();

        if (province != null) {
            Address address = new Address();
            address.setProvince(province);
            if (city != null) {
                address.setCity(city);
            }
            if (county != null) {
                address.setCounty(county);
            }
            if (district != null) {
                address.setDistrict(district);
            }
            if (street != null) {
                address.setStreet(street);
            }
            if (detail != null) {
                address.setDetail(detail);
            }
            addressRepository.save(address);
            user.setAddress(address);
        }

        user.setEmailAddr(emailAddr);
        user.setPassword(pwd);
        if (gender != null) {
            user.setGender(gender);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (date != null) {
            user.setBirthday(date);
        }
        userRepository.save(user);
    }


    /**
     * 更新用户信息(不包括地址)
     *
     * @param emailAddr
     * @param pwd
     * @param gender
     * @param phone
     * @param date
     * @return
     */
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
        }
        else {
            // TODO 用户不存在
            return null;
        }
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
     * 删除一个用户
     *
     * @param emailAddr
     */
    public void deleteUserById(String emailAddr) {
        userRepository.delete(findUserById(emailAddr));
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
        }
        else if (addr != null) {
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
}
