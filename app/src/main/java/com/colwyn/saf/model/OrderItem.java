package com.colwyn.saf.model;

public class OrderItem extends ItemDocID {

    private String UserID;
    private String CurrencyUsed;
    private String OrderTotal;
    private String TimeStamp;
    private String PaymentMethod;
    private String PayPalREF;
    private String Goods;

    //Empty Constructor for Firestore to Work
    public OrderItem() {
    }

    public OrderItem(String userID, String currencyUsed, String orderTotal, String timeStamp, String paymentMethod, String payPalREF, String goods) {

        UserID = userID;
        CurrencyUsed = currencyUsed;
        OrderTotal = orderTotal;
        TimeStamp = timeStamp;
        PaymentMethod = paymentMethod;
        PayPalREF = payPalREF;
        Goods = goods;
    }

    public String getUserID() { return UserID; }
    public void setUserID(String userID) { UserID = userID; }

    public String getCurrencyUsed() { return CurrencyUsed; }
    public void setCurrencyUsed(String currencyUsed) { CurrencyUsed = currencyUsed; }

    public String getOrderTotal() { return OrderTotal; }
    public void setOrderTotal(String orderTotal) { OrderTotal = orderTotal; }

    public String getTimeStamp() { return TimeStamp; }
    public void setTimeStamp(String timeStamp) { TimeStamp = timeStamp; }

    public String getPaymentMethod() { return PaymentMethod; }
    public void setPaymentMethod(String paymentMethod) { PaymentMethod = paymentMethod; }

    public String getPayPalREF() { return PayPalREF; }
    public void setPayPalREF(String payPalREF) { PayPalREF = payPalREF; }

    public String getGoods() { return Goods; }
    public void setGoods(String goods) { Goods = goods; }

}
