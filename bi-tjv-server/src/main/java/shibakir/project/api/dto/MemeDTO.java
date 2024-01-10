package shibakir.project.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemeDTO {

    public MemeDTO(String name, String path_name) {
        this.name = name;
        this.path_name = path_name;
    }

    public String name;      // primary key db
    public String path_name;
    private List<String> users;
    private List<Long> comments;
}
