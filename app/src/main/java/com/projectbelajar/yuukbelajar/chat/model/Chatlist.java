package com.projectbelajar.yuukbelajar.chat.model;

public class Chatlist {
    private String id;
    private String level;

    public Chatlist() {
    }

    public Chatlist(String id, String level) {
        this.id = id;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
