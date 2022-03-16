package com.hp.marketingreport;

public class notificationModel {
    String title,text,date,read;

    public notificationModel(String title, String text, String date, String read) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
