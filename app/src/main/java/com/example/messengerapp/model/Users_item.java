package com.example.messengerapp.model;

public class Users_item {

    private String image_recycler;
    private String name_recycler;
    private String last_message_recycler;
    private String data_recycler;
    private String uId;

    public Users_item(){

    }

    public Users_item (String uId ,String image_recycler , String name_recycler , String last_message_recycler , String data_recycler){
        this.image_recycler = image_recycler;
        this.name_recycler = name_recycler;
        this.last_message_recycler = last_message_recycler;
        this.data_recycler = data_recycler;
        this.setuId(uId);
    }

    public String getImage_recycler() {
        return image_recycler;
    }

    public void setImage_recycler(String image_recycler) {
        this.image_recycler = image_recycler;
    }

    public String getName_recycler() {
        return name_recycler;
    }

    public void setName_recycler(String name_recycler) {
        this.name_recycler = name_recycler;
    }

    public String getLast_message_recycler() {
        return last_message_recycler;
    }

    public void setLast_message_recycler(String last_message_recycler) {
        this.last_message_recycler = last_message_recycler;
    }

    public String getData_recycler() {
        return data_recycler;
    }

    public void setData_recycler(String data_recycler) {
        this.data_recycler = data_recycler;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
