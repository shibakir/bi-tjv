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
    public class UserEntity {

        @Id
        private String username;
        private String password;

        public UserEntity(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @ManyToMany(mappedBy = "users")
        @JsonIgnore
        private List<MemeEntity> memes = new ArrayList<>();

        @JsonIgnore
        @OneToMany(targetEntity = CommentEntity.class, mappedBy = "user", orphanRemoval = true)
        List<CommentEntity> comments = new ArrayList<>();

        @Override
        public String toString() {
            return "UserEntity{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
