package me.kqlqk.todo_list.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import me.kqlqk.todo_list.models.User;

import javax.validation.constraints.Pattern;

public class RegistrationDTO {
    @Pattern(regexp = "^[^\\s@]{3,}@[^\\s@]{2,}\\.[^\\s@]{2,}$", message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^(?=.{5,35}$)(?!.*[_.]{4})[a-zA-Z0-9_]+$",
            message = "Login must be between 5 and 35 characters, only letters, numbers and underscores are allowed, more than \"___\" is not allowed")
    private String login;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$",
            message = "Password must be between 8 and 50 characters, at least 1 number and both lower and uppercase letters")
    private String password;

    private boolean setCookie = true;

    @JsonIgnore
    private String confirmPassword;

    public String getEmail() {
        return email != null ? email.toLowerCase() : email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
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

    @JsonGetter("setCookie")
    public boolean isSetCookie() {
        return setCookie;
    }

    @JsonSetter("setCookie")
    public void setCookie(boolean hasCookie) {
        this.setCookie = hasCookie;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User convertToUser(){
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setPassword(password);
        user.setOAuth2(false);

        return user;
    }
}
