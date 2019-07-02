package com.snail.child.model;

import javax.persistence.*;

/**
 * User: ZhangXinrui
 * Date: 2019/6/27
 * Description: No Description
 */

@Entity
public class SuspectedMissingChild {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer height;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 16777215, nullable = false)
    private byte[] photo;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "missing_address_id")
    private Address missingAddress;

    private String detail;

//    @ManyToOne//(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id",nullable = false)
//    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Address getMissingAddress() {
        return missingAddress;
    }

    public void setMissingAddress(Address missingAddress) {
        this.missingAddress = missingAddress;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

}