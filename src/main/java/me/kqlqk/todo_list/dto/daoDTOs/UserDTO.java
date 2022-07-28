package me.kqlqk.todo_list.dto.daoDTOs;

import me.kqlqk.todo_list.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private long id;
    private String email;
    private String login;
    private long roleId;
    private boolean isOAuth2;

    public UserDTO() {
    }

    public UserDTO(long id, String email, String login, long roleId, boolean isOAuth2) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.roleId = roleId;
        this.isOAuth2 = isOAuth2;
    }

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public boolean isOAuth2() {
        return isOAuth2;
    }

    public void setOAuth2(boolean OAuth2) {
        isOAuth2 = OAuth2;
    }


    public static List<UserDTO> convertListOfUsersToListOfUserDTOs(List<User> users){
        List<UserDTO> userDTOs = new ArrayList<>();
        for(User user : users){
            userDTOs.add(new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getRole().getId(),
                    user.isOAuth2()));
        }

        return userDTOs;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", roleId=" + roleId +
                ", isOAuth2=" + isOAuth2 +
                '}';
    }
}
