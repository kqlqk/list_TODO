package me.kqlqk.springBootApp.validation;

import javax.validation.constraints.Pattern;

public class NoteValidation {

    @Pattern(regexp = "^[^\\s]+(\\s+[^\\s]+){1,100}$", message = "Title message should be a valid")
    private String title;


    private String body;


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

}
