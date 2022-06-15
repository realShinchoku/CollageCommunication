package com.G12LTUDDD.collagecommunication.Models;

import java.util.HashMap;
import java.util.List;

public class Group {
        String gid,name,img;
        List<String> users;
        List<String> admins;
        HashMap<String, Message> messages;
        boolean status = true;

        public  Group(){}

        public Group(String gid, String name, String img, List<String> users, List<String> admins, HashMap<String, Message> messages, boolean status) {
                this.gid = gid;
                this.name = name;
                this.img = img;
                this.users = users;
                this.admins = admins;
                this.messages = messages;
                this.status = status;
        }

        @Override
        public String toString() {
                return "Group{" +
                        "gid='" + gid + '\'' +
                        ", name='" + name + '\'' +
                        ", img='" + img + '\'' +
                        ", users=" + users +
                        ", admins=" + admins +
                        ", messages=" + messages +
                        ", status=" + status +
                        '}';
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

        public void setUsers(List<String> users) {
                this.users = users;
        }

        public List<String> getAdmins() {
                return admins;
        }

        public void setAdmins(List<String> admins) {
                this.admins = admins;
        }

        public HashMap<String, Message> getMessages() {
                return messages;
        }

        public void setMessages(HashMap<String, Message> messages) {
                this.messages = messages;
        }

        public boolean isStatus() {
                return status;
        }

        public void setStatus(boolean status) {
                this.status = status;
        }
}
