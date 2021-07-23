package com.example.bottomnavi;

public class Carditem {
    private int id;
    private int member_id;
    private String title;

    public Carditem(int id, int member_id, String title) {
        this.id = id;
        this.member_id = member_id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
