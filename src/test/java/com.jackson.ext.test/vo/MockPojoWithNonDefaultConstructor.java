package com.jackson.ext.test.vo;

import java.io.Serializable;

public class MockPojoWithNonDefaultConstructor implements Serializable {
    private static final long serialVersionUID = -3839219021803870547L;
    int poiId;
    String comment;
    String userName;
    String phone;

    public MockPojoWithNonDefaultConstructor(int poiId, String comment, String userName, String phone) {
        this.poiId = poiId;
        this.comment = comment;
        this.userName = userName;
        this.phone = phone;
    }

    public int getPoiId() {
        return poiId;
    }

    public void setPoiId(int poiId) {
        this.poiId = poiId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "MockPojoWithNonDefaultConstructor{" +
                "poiId=" + poiId +
                ", comment='" + comment + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
