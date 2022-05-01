package me.kqlqk.springBootApp.service;

import me.kqlqk.springBootApp.DAO.NoteDAO;
import me.kqlqk.springBootApp.models.Note;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteService implements NoteDAO {
    private List<Note> notes;

    {
        notes = new ArrayList<>();
        notes.add(new Note("1st note title", "1st note body"));
        notes.add(new Note("2st note title", "2st note body"));
        notes.add(new Note("3st note title", "3st note body"));
        notes.add(new Note("4st note title", "4st note body"));
    }

    @Override
    public List<Note> getNotes(){
        return notes;
    }

    @Override
    public Note getNote(int id){
        return notes.stream().filter(note -> note.getId() == id).findAny().orElse(null);
    }

    @Override
    public void saveNote(Note note){
        for(int i = 0; i < notes.size(); i++){
            for(int j = 0; j < notes.size(); j++) {
                if (notes.get(j).getId() == note.getId()) {
                    note.setId((int) (Math.random() * 1000));
                }
            }
        }
        notes.add(note);
    }

    @Override
    public boolean existsById(int id){
        Note note = notes.stream().filter(n -> n.getId() == id).findAny().orElse(null);

        if(note == null){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void update(Note note){
        for(int i = 0; i < notes.size(); i++){
            if(notes.get(i).getId() == note.getId()){
                notes.set(i, note);
            }
        }
    }

    @Override
    public void deleteNote(int id){
        notes.removeIf(n -> n.getId() == id);
    }

}
