package com.jwx.travelmantics.models;

import javax.annotation.Nullable;

public class TravelDeal {
    private String title;
    private String price;
    private String description;
    private String image;

    public TravelDeal(){
        // Empty constructor for firebase json data conversion
    }


    public TravelDeal(String title, String price, String description, @Nullable String imageUrl) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = imageUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
