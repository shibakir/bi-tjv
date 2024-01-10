package shibakir.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemeEntity {

    @Id
    private String name;
    private String path_name;

    public MemeEntity(String name, String path_name) {
        this.name = name;
        this.path_name = path_name;
    }

    @ManyToMany
    @JoinTable(
            name = "memes_liked_by_users",
            joinColumns = @JoinColumn(name = "meme_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    List<UserEntity> users = new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = CommentEntity.class, mappedBy = "meme", orphanRemoval = true)
    List<CommentEntity> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "MemeEntity{" +
                "name='" + name + '\'' +
                ", path_name='" + path_name + '\'' +
                '}';
    }
}
