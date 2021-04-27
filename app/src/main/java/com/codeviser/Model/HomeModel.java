package com.codeviser.Model;

public class HomeModel {
    private int image;
    private String name;
    private String title;

    public HomeModel(int image, String name, String title) {
        this.image = image;
        this.name = name;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
