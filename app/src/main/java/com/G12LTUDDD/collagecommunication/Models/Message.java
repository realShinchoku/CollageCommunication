package com.G12LTUDDD.collagecommunication.Models;

import java.time.LocalDateTime;

public class Message {
    String message,name,key;
    LocalDateTime time;

    public  Message(){}

    public Message(String message, String name) {
        this.message = message;
        this.name = name;
        this.time = time;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", time=" + time +
                '}';
    }
}
