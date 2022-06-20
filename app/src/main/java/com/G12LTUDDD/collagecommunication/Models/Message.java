package com.G12LTUDDD.collagecommunication.Models;

import java.util.Date;

public class Message {
    @Override
    public String toString() {
        return "Message{" +
                "gid='" + gid + '\'' +
                ", value='" + value + '\'' +
                ", uid='" + uid + '\'' +
                ", key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String gid,value,uid,key,type;
    Date time;

    public Message(String gid, String value, String uid, String key, Date time, String type) {
        this.gid = gid;
        this.value = value;
        this.uid = uid;
        this.key = key;
        this.time = time;
        this.type = type;
    }

    public Message() {
        this.gid = "";
        this.value = "";
        this.uid = "";
        this.key = "";
        this.time = new Date();
        this.type = "text";
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

}
