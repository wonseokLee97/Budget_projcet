package com.example.bottomnavi;

public class CommentListItem {
    private int id;
    private int member_id;
    private String content;
    private String time;
    private int like_num;
    private String nick_name;
    private int depth;

    public CommentListItem(int id, int member_id, String content, String time, int like_num, String nick_name, int depth){
        this.id = id;
        this.member_id = member_id;
        this.content = content;
        this.time = time;
        this.like_num = like_num;
        this.nick_name = nick_name;
        this.depth = depth;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
