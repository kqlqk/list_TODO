package me.kqlqk.springBootApp.service.impl;

import me.kqlqk.springBootApp.DAO.NoteRepository;
import me.kqlqk.springBootApp.models.Note;
import me.kqlqk.springBootApp.models.User;
import me.kqlqk.springBootApp.service.NoteService;
import me.kqlqk.springBootApp.service.UserService;
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

    private NoteRepository noteRepository;
    private UserService userService;

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
        return noteRepository.existsById(id) ? true : false;
    }

    //NoteService methods
    @Override
    @Transactional
    public void add(Note note) {
        note.setUser(userService.getCurrentUser());
        note.setDateOfCreation(new Timestamp(new java.util.Date().getTime()));
        noteRepository.save(note);
    }

    @Override
    @Transactional
    public void delete(Note note) {
        if (existsById(note.getId())) {
            noteRepository.delete(note);
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (existsById(id)) {
            noteRepository.delete(entityManager.createQuery("from Note where id = " + id, Note.class).getResultList().get(0));
        }
    }

    @Override
    @Transactional
    public void update(Note note) {
        if(existsById(note.getId())){
            note.setDateOfCreation(new Timestamp(new java.util.Date().getTime()));
            note.setUser(userService.getCurrentUser());
            noteRepository.save(note);
        }
    }
}
