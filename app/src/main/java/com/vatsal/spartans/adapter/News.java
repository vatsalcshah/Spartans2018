package com.vatsal.spartans.adapter;

public class News {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public News(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    private String title;
    private String desc;
    private String image;

    public News()
    {

    }
}
