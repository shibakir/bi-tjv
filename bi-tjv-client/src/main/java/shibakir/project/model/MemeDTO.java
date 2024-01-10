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
public class MemeDTO {

    public MemeDTO(String name, String path_name) {
        this.name = name;
        this.path_name = path_name;
        this.users = new ArrayList<>();
    }

    public MemeDTO(String name) {
        this.name = name;
        this.path_name = "";
        this.users = new ArrayList<>();
    }

    public String name; // primary key db
    public String path_name;

    private List<UserDTO> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemeDTO memeDTO = (MemeDTO) o;
        return Objects.equals(name, memeDTO.name);
    }
}
