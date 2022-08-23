package me.kqlqk.todo_list.models;

import javax.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @OneToOne(mappedBy = "refreshToken")
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_in", nullable = false)
    private long expiresIn;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long validity) {
        this.expiresIn = validity;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                '}';
    }
}
