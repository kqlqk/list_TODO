package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.exceptions.dao_exceptions.note_exceptions.NoteAlreadyExistException;
import me.kqlqk.todo_list.exceptions.dao_exceptions.note_exceptions.NoteNotFoundException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

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
        return noteRepository.getById(id);
    }

    @Override
    public List<Note> getByUser(User user) {
        return noteRepository.getByUser(user);
    }

    @Override
    public List<Note> getByUserId(long userId) {
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
        if(existsById(note.getId())) {
            throw new NoteAlreadyExistException("Note already exist, note is" + note);
        }

        if(note.getFullTitle().length() > 36){
            note.setTitle(note.getFullTitle().substring(0,37));
        }
        else {
            note.setTitle(note.getFullTitle());
        }
        note.setUser(userService.getCurrentUser());
        note.setDateOfCreation(new Timestamp(new java.util.Date().getTime()));
        noteRepository.save(note);
        logger.info(note + "was created");
    }

    @Override
    @Transactional
    public void delete(Note note) {
        if (existsById(note.getId())) {
            noteRepository.delete(note);
            logger.info(note + "was deleted");
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (!existsById(id)) {
            throw new NoteNotFoundException("Note not found by id " + id);
        }

        noteRepository.delete(entityManager.createQuery("from Note where id = " + id, Note.class).getResultList().get(0));
        logger.info("Note with id " + id + " was deleted");

    }

    @Override
    public boolean existsForUser(User user, long noteId){
        if(!existsById(noteId)){
            return false;
        }
        return user.getNotes().stream().anyMatch(note -> noteId == note.getId());
    }

    @Override
    @Transactional
    public void update(Note note) {
        if(existsById(note.getId())){
            if(note.getFullTitle().length() > 36){
                note.setTitle(note.getFullTitle().substring(0,37));
            }
            else {
                note.setTitle(note.getFullTitle());
            }
            note.setDateOfCreation(new Timestamp(new java.util.Date().getTime()));
            note.setUser(userService.getCurrentUser());
            noteRepository.save(note);
            logger.info(note + "was updated");
        }
    }
}
