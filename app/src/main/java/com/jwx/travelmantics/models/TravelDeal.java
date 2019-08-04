package com.jwx.travelmantics.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


@IgnoreExtraProperties
public class TravelDeal implements Serializable {
    private String uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("price", price);
        result.put("description", description);
        return result;
    }
}
