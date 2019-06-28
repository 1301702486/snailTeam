package com.snail.child.entity;

import javax.persistence.*;

/**
 * Author: 郭瑞景
 * Date: 2019/6/27
 * Description: Address实体
 */

@Entity
public class Address {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String province;

    private String city;

    private String county;

    private String district;

    private String street;

    private String detail;

//    @OneToOne(mappedBy = "address",cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
//    private User user;
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
