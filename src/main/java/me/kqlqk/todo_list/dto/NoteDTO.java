package me.kqlqk.todo_list.dto;

import me.kqlqk.todo_list.models.Note;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NoteDTO {

    @Size(min = 1, max = 100, message = "Title message should be a valid(must be between 1 and 100 characters)")
    @NotBlank(message = "Title message should be a valid(must be between 1 and 100 characters)")
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
