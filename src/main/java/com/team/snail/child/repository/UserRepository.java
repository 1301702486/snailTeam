package com.team.snail.child.repository;

import com.team.snail.child.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: No description
 */

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmailAddr(String emailAddr);
    User findUserByPhone(String phone);
    List<User> findAllByGender(String gender);
    List<User> findAllByBirthday(Date date);
    List<User> findAllByBirthdayBetween(Date smallDate, Date largeDate);
    List<User> findAllByBirthdayAfter(Date date);
    List<User> findAllByBirthdayBefore(Date date);

    List<User> findAllByAddress_Id(Integer addressId);
}
