package com.vatsal.spartans.adapter;


public class Stock {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Stock(String title, int rate, String image) {
        this.title = title;
        this.rate = rate;
        this.image = image;
    }

    private String title;
    private int rate;
    private String image;

    public Stock() {
    }
}