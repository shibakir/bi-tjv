package shibakir.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {

    @Id
    private Long id;
    private String text;

    @ManyToOne
    @JoinColumn(name = "meme_id", referencedColumnName = "name")
    private MemeEntity meme;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private UserEntity user;
}