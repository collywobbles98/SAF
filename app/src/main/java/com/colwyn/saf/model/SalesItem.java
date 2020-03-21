package com.colwyn.saf.model;

public class SalesItem extends ItemDocID{

    private String ItemID;
    private String ItemTitle;
    private String SellerID;
    private String BuyerID;
    private String BuyerName;
    private String BuyerAddress;
    private String OrderID;
    private String Quantity;
    private String Status;

    //Empty Constructor for Firestore to Work
    public SalesItem() {
    }

    public SalesItem(String itemID, String itemTitle, String sellerID, String buyerID, String buyerName, String buyerAddress, String orderID, String quantity, String status){

        ItemID = itemID;
        ItemTitle = itemTitle;
        SellerID = sellerID;
        BuyerID = buyerID;
        BuyerName = buyerName;
        BuyerAddress = buyerAddress;
        OrderID = orderID;
        Quantity = quantity;
        Status = status;

    }

    public String getItemID() {
        return ItemID;
    }
    public void setItemID(String itemID) { ItemID = itemID; }

    public String getItemTitle() {
        return ItemTitle;
    }
    public void setItemTitle(String itemTitle) { ItemTitle = itemTitle; }

    public String getSellerID() {
        return SellerID;
    }
    public void setSellerID(String sellerID) { SellerID = sellerID; }

    public String getBuyerID() {
        return BuyerID;
    }
    public void setBuyerID(String buyerID) { BuyerID = buyerID; }

    public String getBuyerName() {
        return BuyerName;
    }
    public void setBuyerName(String buyerName) { BuyerName = buyerName; }

    public String getBuyerAddress() {
        return BuyerAddress;
    }
    public void setBuyerAddress(String buyerAddress) { BuyerAddress = buyerAddress; }

    public String getOrderID() {
        return OrderID;
    }
    public void setOrderID(String orderID) { OrderID = orderID; }

    public String getQuantity() {
        return Quantity;
    }
    public void setQuantity(String quantity) { Quantity = quantity; }

    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) { Status = status; }
}
