package me.kqlqk.todo_list.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "last_edited")
    private Timestamp lastEdited;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "full_title", nullable = false)
    private String fullTitle;


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                '}';
    }
}
