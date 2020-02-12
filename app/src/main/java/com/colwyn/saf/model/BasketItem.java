package com.colwyn.saf.model;

public class BasketItem extends BasketDocID {

    private String Item;
    private String UserID;
    private String ImageURL;
    private String Title;
    private String Price;
    private String Delivery_Notes;


    //Empty Constructor for Firestore to Work
    public BasketItem() {
    }

    public BasketItem(String item, String userID, String imageURL, String title, String price, String delivery_Notes) {

        Item = item;
        UserID = userID;
        ImageURL = imageURL;
        Title = title;
        Price = price;
        Delivery_Notes = delivery_Notes;

    }

    public String getItem() {
        return Item;
    }
    public void setItem(String item) { Item = item; }

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

    public String getDelivery_Notes() {
        return Delivery_Notes;
    }
    public void setDelivery_Notes(String delivery_Notes) {
        Delivery_Notes = delivery_Notes;
    }

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) { UserID = userID; }

}
