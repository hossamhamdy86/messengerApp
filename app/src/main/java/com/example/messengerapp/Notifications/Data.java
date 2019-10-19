package com.example.messengerapp.Notifications;

public class Data {
    private String user ;
    private String body ;
    private String title ;
    private String sented ;
    private int icoon ;

    public Data(String user, String body, String title, String sented, int icoon) {
        this.user = user;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.icoon = icoon;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public int getIcoon() {
        return icoon;
    }

    public void setIcoon(int icoon) {
        this.icoon = icoon;
    }
}
