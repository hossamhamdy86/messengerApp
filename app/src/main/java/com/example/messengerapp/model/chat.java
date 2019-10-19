package com.example.messengerapp.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class chat {

    private String sender;
    private String receiver;
    private Timestamp date;
    private String message;

    public chat(String sender, Timestamp date, String message ,String receiver) {
        this.sender = sender;
        this.date = date;
        this.message = message;
        this.receiver = receiver;
    }

    public chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
