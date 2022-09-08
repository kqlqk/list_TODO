package me.kqlqk.todo_list.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Pattern;

/**
 * Represents Data Transfer Object for {@link me.kqlqk.todo_list.models.User}
 */
public class RecoveryDTO {
    @Pattern(regexp = "^[^\\s@]{3,}@[^\\s@]{2,}\\.[^\\s@]{2,}$", message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$",
            message = "Password must be between 8 and 50 characters, at least 1 number and both lower and uppercase letters")
    private String password;

    private String confirmPassword;

    @JsonIgnore
    private boolean formCorrect = true;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isFormCorrect() {
        return formCorrect;
    }

    public void setFormCorrect(boolean formCorrect) {
        this.formCorrect = formCorrect;
    }
}
