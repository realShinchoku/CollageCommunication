package com.G12LTUDDD.collagecommunication.Models;

import java.io.Serializable;

public class User implements Serializable {
    String uid,name, email,img,lop,msv;

    public User() {
        this.uid = "";
        this.name = "";
        this.email = "";
        this.img = "";
        this.lop = "";
        this.msv = "";
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", img='" + img + '\'' +
                ", lop='" + lop + '\'' +
                ", msv='" + msv + '\'' +
                '}';
    }


    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getMsv() {
        return msv;
    }

    public void setMsv(String msv) {
        this.msv = msv;
    }

    public User(String uid, String name, String email, String img, String lop, String msv) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.img = img;
        this.lop = lop;
        this.msv = msv;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
