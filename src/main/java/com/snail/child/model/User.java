package com.snail.child.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.Set;

/**
 * Author: 郭瑞景
 * Date: 2019/6/26
 * Description: User实体
 */

@Entity
public class User {

    @Id
    @Column(nullable = false, length = 125)
    private String emailAddr;

    @Column(nullable = false)
    private String password;

    private String phone;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date birthday;

    private String gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Transient   // 表示不存到数据库中
    private int age;   // 导出属性

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 16777215)
    private byte[] headPortrait;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_find_child_id")
    private ParentFindChild parentFindChild;

    @OneToOne//(cascade=CascadeType.ALL)
    @JoinColumn(name = "child_find_parent_id")
    private ChildFindParent childFindParent;


    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SuspectedMissingChild> suspectedMissingChildren;


    public byte[] getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(byte[] headPortrait) {
        this.headPortrait = headPortrait;
    }

    public int getAge() {
        return age;
    }

    /**
     * 由生日计算年龄
     */
    private void setAge() {
        int age = 0;
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH);
        int currDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(getBirthday());
        int birthYear = calendar.get(Calendar.YEAR);
        int birthMonth = calendar.get(Calendar.MONTH);
        int birthDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (currMonth > birthMonth) {
            age = currYear - birthYear;
        } else if (currMonth == birthMonth) {
            if (currDay >= birthDay) {
                age = currYear - birthYear;
            } else {
                age = currYear - birthYear - 1;
            }
        } else {
            age = currYear - birthYear - 1;
        }
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
        setAge();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public ParentFindChild getParentFindChild() {
        return parentFindChild;
    }

    public void setParentFindChild(ParentFindChild parentFindChild) {
        this.parentFindChild = parentFindChild;
    }

    public ChildFindParent getChildFindParent() {
        return childFindParent;
    }

    public void setChildFindParent(ChildFindParent childFindParent) {
        this.childFindParent = childFindParent;

    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    public Set<SuspectedMissingChild> getSuspectedMissingChildren() {
        return suspectedMissingChildren;
    }

    public void setSuspectedMissingChildren(Set<SuspectedMissingChild> suspectedMissingChildren) {
        this.suspectedMissingChildren = suspectedMissingChildren;
    }

    public void addSuspectedMissingChild(SuspectedMissingChild suspectedMissingChild) {
        this.suspectedMissingChildren.add(suspectedMissingChild);
//        if(suspectedMissingChild.getUser()!=this){
//            suspectedMissingChild.setUser(this);
//        }
    }
}
