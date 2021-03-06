package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotFoundException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteAlreadyExists;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @PersistenceContext
    private EntityManager entityManager;

    private final NoteRepository noteRepository;
    private final UserService userService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }
    //JPA-repository methods
    @Override
    public Note getById(long id) {
        if(id < 1){
            throw new NoteNotFoundException("note id cannot be lower 1");
        }
        return noteRepository.getById(id);
    }

    @Override
    public List<Note> getByUser(User user) {
        if(!(userService.existsByEmail(user.getEmail()) || userService.existsByLogin(user.getLogin()))){
            throw new UserNotFoundException(user + " isn't exist");
        }
        return noteRepository.getByUser(user);
    }

    @Override
    public List<Note> getByUserId(long userId) {
        if(!(userService.existsById(userId))){
            throw new UserNotFoundException("User with id = " + userId + " not found");
        }
        return noteRepository.getByUserId(userId);
    }

    @Override
    public boolean existsById(long id) {
        return noteRepository.existsById(id);
    }

    //NoteService methods
    @Override
    @Transactional
    public void add(Note note) {
        if(noteRepository.existsById(note.getId())){
            throw new NoteAlreadyExists(note + " already exists");
        }

        if(note.getFullTitle().length() > 36){
            note.setTitle(note.getFullTitle().substring(0,37));
        }
        else {
            note.setTitle(note.getFullTitle());
        }
        note.setUser(userService.getCurrentUser());
        note.setLastEdited(new Timestamp(new java.util.Date().getTime()));
        noteRepository.save(note);
    }

    @Override
    @Transactional
    public void delete(Note note) {
        if(!noteRepository.existsById(note.getId())){
            throw new NoteNotFoundException(note + " doesn't exist");
        }
        noteRepository.delete(note);
    }

    @Override
    @Transactional
    public void delete(long id) {
        if(!noteRepository.existsById(id)){
            throw new NoteNotFoundException("Note with id = " + id + " doesn't exist");
        }
        noteRepository.delete(entityManager.createQuery("from Note where id = " + id, Note.class).getResultList().get(0));
    }

    @Override
    public boolean existsForUser(User user, long noteId){
        return user.getNotes().stream().anyMatch(note -> noteId == note.getId());
    }

    @Override
    @Transactional
    public void update(Note note) {
        if(!noteRepository.existsById(note.getId())){
            throw new NoteNotFoundException(note + " doesn't exist");
        }

        if(note.getFullTitle().length() > 36){
            note.setTitle(note.getFullTitle().substring(0,37));
        }
        else {
            note.setTitle(note.getFullTitle());
        }
        note.setLastEdited(new Timestamp(new java.util.Date().getTime()));
        noteRepository.save(note);
    }
}
