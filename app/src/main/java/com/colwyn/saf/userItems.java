package com.colwyn.saf;

public class userItems {

    String Title, Price, ImageURL, Condition, Brand, Category, Description, Delivery_Notes, UserID;

    public userItems(String title, String price, String imageURL, String condition, String brand, String category, String description, String delivery_Notes, String userID) {
        Title = title;
        Price = price;
        ImageURL = imageURL;
        Condition = condition;
        Brand = brand;
        Category = category;
        Description = description;
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
