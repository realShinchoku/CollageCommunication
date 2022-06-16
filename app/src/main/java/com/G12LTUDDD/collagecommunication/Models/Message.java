package com.G12LTUDDD.collagecommunication.Models;

import java.util.Date;

public class Message {
    String gid,value,uid,key;
    Date time;

    public Message(String gid, String value, String uid, String key, Date time) {
        this.gid = gid;
        this.value = value;
        this.uid = uid;
        this.key = key;
        this.time = time;
    }

    public Message() {
        this.gid = "";
        this.value = "";
        this.uid = "";
        this.key = "";
        this.time = new Date();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "gid='" + gid + '\'' +
                ", value='" + value + '\'' +
                ", uid='" + uid + '\'' +
                ", key='" + key + '\'' +
                ", time=" + time +
                '}';
    }
}
