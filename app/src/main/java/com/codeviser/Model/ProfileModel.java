package com.codeviser.Model;

public class ProfileModel {
    private int image;
    private String title;
    private String name;

    public ProfileModel(int image, String title, String name) {
        this.image = image;
        this.title = title;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
