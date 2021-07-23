package com.example.bottomnavi;

public class PostListItem {
    private int id;
    private int member_id;
    private String nick_name;
    private String title;
    private String content;
    private String post_time;
    private int like_num;

    public PostListItem(int id, int member_id, String nick_name, String title, String content, String post_time, int like_num) {
        this.id = id;
        this.member_id = member_id;
        this.nick_name = nick_name;
        this.title = title;
        this.content = content;
        this.post_time = post_time;
        this.like_num = like_num;
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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }
}