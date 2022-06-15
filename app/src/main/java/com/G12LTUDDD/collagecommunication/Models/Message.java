package com.G12LTUDDD.collagecommunication.Models;

import com.google.firebase.Timestamp;

public class Message {
    String message,name,key;
    Timestamp time;
    boolean isDelete = false;

    public  Message(){}

    public Message(String message, String name, String key, Timestamp time, boolean isDelete) {
        this.message = message;
        this.name = name;
        this.key = key;
        this.time = time;
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", time=" + time +
                ", isDelete=" + isDelete +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
