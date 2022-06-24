package com.G12LTUDDD.collagecommunication.Models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group implements Serializable {
    String gid, name, img, lastMsg,own;
    List<String> users;
    List<String> admins;
    Date modTime;
    boolean status,isPinned;


    public String getOwn() {
        return own;
    }

    public void setOwn(String own) {
        this.own = own;
    }


    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public Group() {
        this.gid = "";
        this.name = "";
        this.img = "";
        this.lastMsg = "";
        this.users = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.modTime = new Date();
        this.status = true;
        this.isPinned = false;
        this.own = "";
    }

    public Group(String gid, String name, String img, String lastMsg, String own, List<String> users, List<String> admins, Date modTime, boolean status, boolean isPinned) {
        this.gid = gid;
        this.name = name;
        this.img = img;
        this.lastMsg = lastMsg;
        this.own = own;
        this.users = users;
        this.admins = admins;
        this.modTime = modTime;
        this.status = status;
        this.isPinned = isPinned;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Group{" +
                "gid='" + gid + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", lastMsg='" + lastMsg + '\'' +
                ", own='" + own + '\'' +
                ", users=" + users +
                ", admins=" + admins +
                ", modTime=" + modTime +
                ", status=" + status +
                ", isPinned=" + isPinned +
                '}';
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
