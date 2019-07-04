package com.snail.child.model;

/**
 * User: ZhangXinrui
 * Date: 2019/6/27
 * Description: No Description
 */

import javax.persistence.*;
import java.util.Date;

@Entity
public class ParentFindChild {

    @Id
    @GeneratedValue
    private Integer id;//发布id

    @Column(nullable = false)
    private String name;//姓名

    //@Column(nullable = false)
    @Temporal(TemporalType.DATE)
    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;//生日

    //@Column(nullable = false)
    @Temporal(TemporalType.DATE)
    //@JsonFormat(pattern ="yyyy/MM/dd ", timezone = "GMT+8")
    private Date missingDate;//走失时间

    @Column(nullable = false)
    private String gender;//性别

//    public byte[] getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(byte[] photo) {
//        this.photo = photo;
//    }

//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(length = 16777215,nullable = false)
//    private byte[] photo;//图片

    private String photo;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "missing_address_id")
    private Address missingAddress;


    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "home_address_id")
    private Address homeAddress;

    private String detail;//其他详情

//    @OneToOne (cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
//    //@OneToOne(mappedBy = "parentFindChild")
//    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getMissingDate() {
        return missingDate;
    }

    public void setMissingDate(Date missingDate) {
        this.missingDate = missingDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Address getMissingAddress() {
        return missingAddress;
    }

    public void setMissingAddress(Address missingAddress) {
        this.missingAddress = missingAddress;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
