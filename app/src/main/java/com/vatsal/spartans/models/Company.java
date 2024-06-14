package com.vatsal.spartans.models;


public class Company {
    public String id = "z";
    public String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int rate;
    public String title;

    Company(){}

    public Company(String image, int rate, String title) {
        this.image = image;
        this.rate = rate;
        this.title = title;
    }

    @Override
    public String toString() {
        return  "Company ["+id+"] : { " + image + ", " + rate + ", " + title + " }";
    }
}
