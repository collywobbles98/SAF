package com.colwyn.saf.model;

public class ChatItem extends ItemDocID {

    private String User1;
    private String User1FirstName;
    private String User1LastName;
    private String User1Initials;

    private String User2;
    private String User2FirstName;
    private String User2LastName;
    private String User2Initials;

    private String LastMessage;

    //Empty Constructor for Firestore to Work
    public ChatItem() {
    }

    public ChatItem(String user1, String user1FirstName, String user1LastName, String user1Initials, String user2, String user2FirstName, String user2LastName, String user2Initials, String lastMessage){

        User1 = user1;
        User1FirstName = user1FirstName;
        User1LastName = user1LastName;
        User1Initials = user1Initials;

        User2 = user2;
        User2FirstName = user2FirstName;
        User2LastName = user2LastName;
        User2Initials = user2Initials;

        LastMessage = lastMessage;

    }

    public String getUser1() {
        return User1;
    }
    public void setUser1(String user1) { User1 = user1; }

    public String getUser1FirstName() {
        return User1FirstName;
    }
    public void setUser1FirstNamw(String user1FirstName) { User1FirstName = user1FirstName; }

    public String getUser1LastName() {
        return User1LastName;
    }
    public void setUser1LastName(String user1LastName) { User1LastName = user1LastName; }

    public String getUser1Initials() {
        return User1Initials;
    }
    public void setUser1Initials(String user1Initials) { User1Initials = user1Initials; }

    public String getUser2() { return User2; }
    public void setUser2(String user2) { User2 = user2; }

    public String getUser2FirstName() {
        return User2FirstName;
    }
    public void setUser2FirstName(String user2FirstName) { User2FirstName = user2FirstName; }

    public String getUser2LastName() { return User2LastName; }
    public void setUser2LastName(String user2LastName) { User2LastName = user2LastName; }

    public String getUser2Initials() {
        return User2Initials;
    }
    public void setUser2Initials(String user2Initials) { User2Initials = user2Initials; }

    public String getLastMessage() { return LastMessage; }
    public void setLastMessage(String lastMessage) { LastMessage = lastMessage; }

}
