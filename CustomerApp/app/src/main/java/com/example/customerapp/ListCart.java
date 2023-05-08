package com.example.customerapp;

public class ListCart {
    String id, name, des,img, price, numItem;

    public ListCart(String id, String name, String des, String img, String price, String numItem) {

        this.id = id;
        this.name = name;
        this.des = des;
        this.img = img;
        this.price = price;
        this.numItem = numItem;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getDes() {
        return des;
    }

    public String getPrice() {
        return price;
    }

    public String getNumItem() {
        return numItem;
    }

}
