package com.G12LTUDDD.collagecommunication.Models;

import java.util.List;

public class Group {
        String gid,name;
        List<String> users;
        List<String> admins;
        List<Message> messages;
        boolean status = true;

        public Group(){}

        public Group(String gid, String name, List<String> users, List<String> admins, List<Message> messages) {
                this.gid = gid;
                this.name = name;
                this.users = users;
                this.admins = admins;
                this.messages = messages;
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

        public List<Message> getMessages() {
                return messages;
        }

        public void setMessages(List<Message> messages) {
                this.messages = messages;
        }

        public boolean isStatus() {
                return status;
        }

        public void setStatus(boolean status) {
                this.status = status;
        }

        @Override
        public String toString() {
                return "Group{" +
                        "gid='" + gid + '\'' +
                        ", name='" + name + '\'' +
                        ", users=" + users +
                        ", admins=" + admins +
                        ", messages=" + messages +
                        ", status=" + status +
                        '}';
        }
}
