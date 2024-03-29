package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.aspects.LoggingAspect;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteAlreadyExistsException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotValidException;
import me.kqlqk.todo_list.models.Note;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.repositories.NoteRepository;
import me.kqlqk.todo_list.service.NoteService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Represents implementation for {@link me.kqlqk.todo_list.service.NoteService}
 */
@Service
public class NoteServiceImpl implements NoteService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final NoteRepository noteRepository;
    private final UserService userService;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }


    /**
     * @return {@link me.kqlqk.todo_list.models.Note} OR null, if {@link me.kqlqk.todo_list.models.Note} not found
     */
    @Override
    public Note getById(long id) {
        return noteRepository.findById(id);
    }

    /**
     * @return List of {@link me.kqlqk.todo_list.models.Note} OR empty list
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotValidException if {@link me.kqlqk.todo_list.models.User} isn't valid
     */
    @Override
    public List<Note> getByUser(User user) {
        if(!userService.isValid(user)){
            throw new UserNotValidException("User = " + user + " not valid");
        }
        return noteRepository.findByUser(user);
    }

    @Override
    public boolean existsById(long id) {
        return noteRepository.existsById(id);
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException if {@linkplain  Note#getFullTitle()} is null,
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteAlreadyExistsException if {@link me.kqlqk.todo_list.models.Note} already exists
     */
    @Override
    public void add(Note note) {
        if(note.getFullTitle() == null){
            throw new NoteNotValidException("Note isn't valid. Note should has at least full title. \n" +
                    "full title = " + note.getFullTitle());
        }
        if(existsById(note.getId())){
            throw new NoteAlreadyExistsException("Note with id = " + note.getId() + " already exists");
        }


        if(note.getFullTitle().length() > 37){
            note.setTitle(note.getFullTitle().substring(0,37));
        }
        else {
            note.setTitle(note.getFullTitle());
        }
        note.setUser(userService.getCurrentUser());
        note.setLastEdited(new Timestamp(new Date().getTime()));

        noteRepository.save(note);

        logger.info("Was crated note " + note);
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException if {@link me.kqlqk.todo_list.models.Note} not found
     */
    @Override
    public void delete(Note note) {
        if(!isValid(note)) {
            throw new NoteNotValidException("Note with id = " + note.getId() + " not found");
        }

        noteRepository.delete(note);

        logger.info("Was deleted note " + note);
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException if {@link me.kqlqk.todo_list.models.Note} not found
     */
    @Override
    public void delete(long id) {
        if(!isValid(id)){
            throw new NoteNotValidException("Note with id = " + id + " not found");
        }

        Note note = getById(id);

        noteRepository.delete(note);

        logger.info("Was deleted note " + note);
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException if {@link me.kqlqk.todo_list.models.Note} not found OR isn't available for current {@link me.kqlqk.todo_list.models.User},
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotValidException if {@link me.kqlqk.todo_list.models.User} not valid
     */
    @Override
    public boolean existsForUser(User user, long noteId){
        if(!userService.isValid(user)){
            throw new UserNotValidException("User not valid");
        }
        if(!isValid(noteId)){
            throw new NoteNotValidException("Note not found OR isn't available for current user");
        }

        return user.getNotes().stream().anyMatch(note -> noteId == note.getId());
    }

    /**
     * @throws me.kqlqk.todo_list.exceptions_handling.exceptions.note.NoteNotValidException if {@link me.kqlqk.todo_list.models.Note} not found
     */
    @Override
    public void update(Note note) {
        if(!isValid(note)){
            throw new NoteNotValidException("Note with id = " + note.getId() + " not found");
        }

        if(note.getFullTitle().length() > 37){
            note.setTitle(note.getFullTitle().substring(0,37));
        }
        else {
            note.setTitle(note.getFullTitle());
        }
        note.setLastEdited(new Timestamp(new java.util.Date().getTime()));

        noteRepository.save(note);

        logger.info("Was updated note " + note);
    }

    @Override
    public boolean isValid(Note note){
        if(note == null || !existsById(note.getId()) || note.getFullTitle() == null || note.getId() == 0 || note.getUser() == null){
            return false;
        }

        return true;
    }

    @Override
    public boolean isValid(long noteId){
        if(!existsById(noteId)){
            return false;
        }

        return isValid(getById(noteId));
    }
}
