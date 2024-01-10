package shibakir.project.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username; // primary key db
    public String password;

    private List<String> memes;
    private List<Long> comments;
}
