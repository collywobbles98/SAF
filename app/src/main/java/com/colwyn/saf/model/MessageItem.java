package com.colwyn.saf.model;

public class MessageItem extends ItemDocID {

    private String Sender;
    private String Reciever;
    private String ChatID;
    private String Message;
    private String TimeStamp;

    //Empty Constructor for Firestore to Work
    public MessageItem() {
    }

    public MessageItem(String sender, String reciever, String chatID, String message, String timeStamp){

        Sender = sender;
        Reciever = reciever;
        ChatID = chatID;
        Message = message;
        TimeStamp = timeStamp;

    }

    public String getSender() {
        return Sender;
    }
    public void setSender(String sender) { Sender = sender; }

    public String getReciever() {
        return Reciever;
    }
    public void setReciever(String reciever) { Reciever = reciever; }

    public String getChatID() {
        return ChatID;
    }
    public void setChatID(String chatID) { ChatID = chatID; }

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) { Message = message; }

    public String getTimeStamp() { return TimeStamp; }
    public void setTimeStamp(String timeStamp) { TimeStamp = timeStamp; }

}
