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

//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(length = 16777215, nullable = false)
//    private byte[] photo;

    private String photo;

    private String faceToken;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "missing_address_id")
    private Address missingAddress;

    private String detail;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }
}