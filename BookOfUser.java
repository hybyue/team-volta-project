package com.example.volta_lang.BookingAdapterData;

public class BookOfUser {
    String currentDate;
    String dateSet;
    String days;
    String name;
    String price, totalGuest, totalPrice, imageUrl;

    private String documentId;



    public BookOfUser(String currentDate, String dateSet, String days, String name, String price, String totalGuest, String totalPrice, String imageUrl) {
        this.currentDate = currentDate;
        this.dateSet = dateSet;
        this.days = days;
        this.name = name;
        this.price = price;
        this.totalGuest = totalGuest;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getDateSet() {
        return dateSet;
    }

    public void setDateSet(String dateSet) {
        this.dateSet = dateSet;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalGuest() {
        return totalGuest;
    }

    public void setTotalGuest(String totalGuest) {
        this.totalGuest = totalGuest;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    // Add a setter method for the document ID
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public BookOfUser(){

    }
}
