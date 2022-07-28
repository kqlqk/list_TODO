package me.kqlqk.todo_list.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LoginDTO {
    private String loginObj;
    private String password;
    private boolean setCookie = true;
    @JsonIgnore
    private boolean isFormCorrect = true;

    public String getLoginObj() {
        return loginObj;
    }

    public void setLoginObj(String loginObj) {
        this.loginObj = loginObj;
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

    public boolean isFormCorrect() {
        return isFormCorrect;
    }

    public void setFormCorrect(boolean formCorrect) {
        isFormCorrect = formCorrect;
    }
}
