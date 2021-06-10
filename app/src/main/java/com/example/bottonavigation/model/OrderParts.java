package com.example.bottonavigation.model;

public class OrderParts extends BaseParts {
    private int qty;
    private double totalPrice;
    private String imgPath, desc;
    private String categoryName;
    public OrderParts(int categoryId, String id, String name, Double price, int qty, String imgPath, String desc) {
        super(id, name, categoryId, price, qty);
        this.qty = qty;
        this.desc = desc;
        this.imgPath = imgPath;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

