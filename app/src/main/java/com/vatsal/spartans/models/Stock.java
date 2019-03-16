package com.vatsal.spartans.models;

/**
 * Mock Stock
 * Created by Yash on 9/15/2018.
 */
public class Stock {
    String id;
    String key;
    int boughtAt = 0, curentPrice = 0;

    public int getBoughtAt() {
        return boughtAt;
    }

    public Stock setBoughtAt(int boughtAt) {
        this.boughtAt = boughtAt;
        return this;
    }

    public int getCurentPrice() {
        return curentPrice;
    }

    public Stock setCurentPrice(int curentPrice) {
        this.curentPrice = curentPrice;
        return this;
    }

    public Stock(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Stock setId(String id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Stock setKey(String key) {
        this.key = key;
        return this;
    }
}
