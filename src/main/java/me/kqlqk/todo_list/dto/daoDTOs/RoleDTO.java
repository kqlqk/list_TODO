package me.kqlqk.todo_list.dto.daoDTOs;

/**
 * Represents Data Transfer Object for {@link me.kqlqk.todo_list.models.Role}
 */
public class RoleDTO {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
