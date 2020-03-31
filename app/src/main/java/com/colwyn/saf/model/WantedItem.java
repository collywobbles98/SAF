package com.colwyn.saf.model;

public class WantedItem extends ItemDocID {

    private String PosterID;
    private String Title;
    private String Description;
    private String TimeStamp;

    //Empty Constructor for Firestore to Work
    public WantedItem() {
    }

    public WantedItem(String posterID, String title, String description, String timeStamp) {

        PosterID = posterID;
        Title = title;
        Description = description;
        TimeStamp = timeStamp;

    }

    public String getPosterID() { return PosterID; }
    public void setPosterID(String posterID) { PosterID = posterID; }

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

    public String getTimeStamp() { return TimeStamp; }
    public void setTimeStamp(String timeStamp) { TimeStamp = timeStamp; }



}
