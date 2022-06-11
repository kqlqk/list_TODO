package me.kqlqk.todo_list.validation;

import me.kqlqk.todo_list.models.Note;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NoteValidation {

    @Size(min = 1, max = 100, message = "Title message should be a valid(Cannot starts or end with space)")
    @NotBlank(message = "Title message should be a valid(Cannot starts or end with space)")
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

    public Note convertToNote(){
        Note note = new Note();
        note.setFullTitle(title.trim());
        note.setBody(body);

        return note;
    }

}
