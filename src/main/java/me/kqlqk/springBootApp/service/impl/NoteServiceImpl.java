package me.kqlqk.springBootApp.service.impl;

import me.kqlqk.springBootApp.DAO.NoteDAO;
import me.kqlqk.springBootApp.models.Note;
import me.kqlqk.springBootApp.models.User;
import me.kqlqk.springBootApp.service.NoteService;
import me.kqlqk.springBootApp.service.UserService;
import me.kqlqk.springBootApp.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class NoteServiceImpl extends SessionUtil implements NoteService {
    private NoteDAO noteDAO;
    private UserService userService;

    @Autowired
    public NoteServiceImpl(NoteDAO noteDAO, UserService userService) {
        this.noteDAO = noteDAO;
        this.userService = userService;
    }


    @Override
    public void add(Note note) {
        openTransactionSession();
        note.setUser(userService.getCurrentUser());
        note.setDateOfCreation(new Date(new java.util.Date().getTime()));
        getSession().persist(note);
        closeTransactionSession();
    }

    @Override
    public void delete(Note note) {
        if(getSession() == null && !getSession().isOpen()) {
            if (existsById(note.getId())) {
                openTransactionSession();
                getSession().remove(note);
                closeTransactionSession();
            }
        }
    }

    @Override
    public void delete(long id) {
        openTransactionSession();
        if (existsById(id)) {
            openTransactionSession();
            getSession().remove(getSession().createQuery("from Note where id = " + id, Note.class).list().get(0));
        }
        closeTransactionSession();
    }

    @Override
    public boolean existsById(long id) {
        if(getById(id) == null){
            return false;
        }
        return true;
    }

    @Override
    public Note getById(long id) {
        return noteDAO.getById(id);
    }

    @Override
    public List<Note> getByUser(User user) {
        openTransactionSession();
        List<Note> res = getSession().createQuery("from Note where user.id = " + user.getId(), Note.class).list();
        closeTransactionSession();
        return res;
    }

    @Override
    public List<Note> getByUserId(long userId) {
        openTransactionSession();
        List<Note> res = getSession().createQuery("from Note where user.id = " + userId, Note.class).list();
        closeTransactionSession();
        return res;
    }

    @Override
    public void update(Note note) {
        if(existsById(note.getId())){
            openTransactionSession();
            note.setDateOfCreation(new Date(new java.util.Date().getTime()));
            getSession().merge(note);
            closeTransactionSession();
        }
    }
}
