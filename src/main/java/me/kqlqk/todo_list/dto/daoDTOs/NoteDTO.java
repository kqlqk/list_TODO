package me.kqlqk.todo_list.dto.daoDTOs;

import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.service.NoteService;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NoteDTO {

    private long id;

    @Size(min = 1, max = 100, message = "Title message should be a valid(must be between 1 and 100 characters)")
    private String title;

    @Size(max = 65000, message = "Message is too long")
    private String body;

    private Timestamp lastEdited;

    private long userId;


    public NoteDTO(){
    }

    public NoteDTO(String title, String body){
        this.title = title;
        this.body = body;
    }

    public NoteDTO(long id, String title, String body, Timestamp lastEdited, long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.lastEdited = lastEdited;
        this.userId = userId;
    }

    public NoteDTO(Note note){
        this.id = note.getId();
        this.title = note.getTitle();
        this.body = note.getBody();
        this.lastEdited = note.getLastEdited();
        this.userId = note.getUser().getId();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Timestamp getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Timestamp lastEdited) {
        this.lastEdited = lastEdited;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public Note convertToNewNote(){
        Note note = new Note();

        note.setFullTitle(title.trim());
        note.setBody(body);

        return note;
    }

    public Note convertToEditedNote(NoteService noteService, long id) {
        Note note = noteService.getById(id);
        if(title == null){
            note.setBody(body);
        }
        else if(body == null) {
            note.setFullTitle(title);
        }
        else{
            note.setFullTitle(title);
            note.setBody(body);
        }
        return note;
    }

    public static List<NoteDTO> convertListOfNotesToListOfNoteDTOs(List<Note> notes){
        List<NoteDTO> noteDTOs = new ArrayList<>();
        for(Note note : notes){
            noteDTOs.add(new NoteDTO(
                    note.getId(),
                    note.getTitle(),
                    note.getBody(),
                    note.getLastEdited(),
                    note.getUser().getId()));
        }

        return noteDTOs;
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", last edited='" + lastEdited + '\'' +
                ", userId=" + userId +
                '}';
    }
}
