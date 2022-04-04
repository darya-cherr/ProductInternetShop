package com.example.productinternetshop.Parser;

public class ParseItem {
    private String imgUrl, brand, name, price;

    public ParseItem(){

    }

    public ParseItem(String imgUrl, String brand, String name, String price) {
        this.imgUrl = imgUrl;
        this.brand = brand;
        this.name = name;
        this.price = price;
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
}
