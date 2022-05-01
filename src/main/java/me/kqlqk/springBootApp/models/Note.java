package me.kqlqk.springBootApp.models;

import java.util.Date;

public class Note {
    private int id;
    private String title;
    private String body;
    private Date dateOfCreation;

    public Note(){
        this.dateOfCreation = new Date();
        this.id = (int) (Math.random() * 1000);
    }
    public Note(String title, String body) {
        this.id = (int) (Math.random() * 1000);
        this.title = title;
        this.body = body;
        this.dateOfCreation = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
