package com.colwyn.saf.model;

public class UserItem extends ItemDocID{

    private String ImageURL;
    private String Title;
    private String Category;
    private String Description;
    private String Brand;
    private String Condition;
    private String Price;
    private String Delivery_Notes;
    private String UserID;

    //Empty Constructor for Firestore to Work
    public UserItem() {
    }

    public UserItem(String imageURL, String title, String category, String description, String brand, String condition, String price, String delivery_Notes, String userID) {
        ImageURL = imageURL;
        Title = title;
        Category = category;
        Description = description;
        Brand = brand;
        Condition = condition;
        Price = price;
        Delivery_Notes = delivery_Notes;
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDelivery_Notes() {
        return Delivery_Notes;
    }

    public void setDelivery_Notes(String delivery_Notes) {
        Delivery_Notes = delivery_Notes;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
