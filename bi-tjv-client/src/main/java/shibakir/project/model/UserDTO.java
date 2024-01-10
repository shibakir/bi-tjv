package shibakir.project.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
        this.memes = new ArrayList<>();
    }

    public UserDTO(String username) {
        this.username = username;
        this.password = "";
        this.memes = new ArrayList<>();
    }

    public String username; // primary key db
    public String password;

    private List<MemeDTO> memes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(username, userDTO.username);
    }
}
