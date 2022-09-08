package me.kqlqk.todo_list.models;


import javax.persistence.*;
import java.util.List;

/**
 * Represents Data Access Object
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false, insertable = false)
    private long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    @Column(name = "oauth2", nullable = false)
    private boolean isOAuth2;

    @OneToOne
    @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
    private RefreshToken refreshToken;

    @Transient
    private String confirmPassword;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public boolean isOAuth2() {
        return isOAuth2;
    }

    public void setOAuth2(boolean OAuth2) {
        isOAuth2 = OAuth2;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", isOAuth2='" + isOAuth2 + '\'' +
                '}';
    }
}
