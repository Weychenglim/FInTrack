package com.example.fintrack.db;

public class TipsItem {
    String title, type, webLink;
    int imageId;

    public TipsItem(String title, String type, String webLink, int imageId) {
        this.title = title;
        this.type = type;
        this.webLink = webLink;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
