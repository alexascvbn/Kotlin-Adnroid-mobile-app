package com.example.bottonavigation.model;

import java.util.ArrayList;

public class NewsModel {
    private int typeId;
    private String title, subtitle, image_url, source_url;
    private ArrayList<NewsModel> singleItemList;
    public NewsModel() {

    }

    public NewsModel (String title, ArrayList<NewsModel> singleItemList) {
        this.title = title;
        this.singleItemList = singleItemList;
    }

    public NewsModel(int typeId, String title, String image_url, String source_url) {
        this.typeId = typeId;
        this.title = title;
        this.image_url = image_url;
        this.source_url = source_url;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public ArrayList<NewsModel> getSingleItemList() {
        return singleItemList;
    }

    public void setSingleItemList(ArrayList<NewsModel> singleItemList) {
        this.singleItemList = singleItemList;
    }

    public void appendNews(NewsModel newsModel) {
        this.singleItemList.add(newsModel);
    }

    public void clearNews() {
        this.singleItemList.clear();
    }

}
