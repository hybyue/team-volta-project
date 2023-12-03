package com.example.volta_lang.BookingAdapterData;

public class AcceptedBookings {

    String currentDate;
    String dateSet;
    String days;
    String name, username, userGmail;
    String  totalGuest, totalPrice, imageUrl, time;



    private String documentId;


    public AcceptedBookings(String currentDate, String dateSet, String days, String name, String price, String totalGuest, String totalPrice, String imageUrl, String username, String time, String userGmail) {
        this.currentDate = currentDate;
        this.dateSet = dateSet;
        this.days = days;
        this.name = name;
        this.totalGuest = totalGuest;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
        this.username =  username;
        this.time = time;
        this.userGmail =userGmail;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserGmail() {
        return userGmail;
    }

    public void setUserGmail(String userGmail) {
        this.userGmail = userGmail;
    }

    public AcceptedBookings(){

    }
}
