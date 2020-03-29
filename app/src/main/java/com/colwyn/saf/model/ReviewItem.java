package com.colwyn.saf.model;

public class ReviewItem extends ItemDocID {

    private String Item;
    private String UserID;
    private String Reviewer;
    private String Title;
    private String Description;
    private String Stars;

    //Empty Constructor for Firestore to Work
    public ReviewItem() {
    }

    public ReviewItem(String item, String userID, String reviewer, String title, String description, String stars) {

        Item = item;
        UserID = userID;
        Reviewer = reviewer;
        Title = title;
        Description = description;
        Stars = stars;

    }

    public String getItem() { return Item; }
    public void setItem(String item) { Item = item; }

    public String getUserID() { return UserID; }
    public void setUserID(String userID) { UserID = userID; }

    public String getReviewer() { return Reviewer; }
    public void setReviewer(String reviewer) { Reviewer = reviewer; }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getStars() { return Stars; }
    public void setStars(String stars) { Stars = stars; }


}
