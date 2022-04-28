package com.example.productinternetshop.Parser;

public class ParseItem {
    private String imgUrl, brand, name, price, detailUrl, favStatus;
    private int key_id;

    public ParseItem(){

    }

    public ParseItem(String imgUrl, String brand, String name, String price, String detailUrl, String favStatus, int key_id) {
        this.imgUrl = imgUrl;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.detailUrl = detailUrl;
        this.favStatus = favStatus;
        this.key_id = key_id;
    }

    public ParseItem(String imgUrl, String brand, String name, String price, int key_id) {
        this.imgUrl = imgUrl;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.key_id = key_id;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
