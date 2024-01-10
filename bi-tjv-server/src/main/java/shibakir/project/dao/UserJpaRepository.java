package shibakir.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.List;
import java.util.Set;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u NOT IN (SELECT u FROM UserEntity u JOIN u.memes m WHERE m.name = :memeName)")
    List<UserEntity> findUsersNotLinkedToMeme(String memeName);

    @Query("SELECT u FROM MemeEntity u JOIN u.users m WHERE m.username = ?1")
    Set<MemeEntity> findMemesByUsername(String username);

}
