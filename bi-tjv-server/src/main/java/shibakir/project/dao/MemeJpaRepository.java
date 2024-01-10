package shibakir.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.List;
import java.util.Set;

public interface MemeJpaRepository extends JpaRepository<MemeEntity, String> {

    @Query("SELECT COUNT(u) FROM MemeEntity m JOIN m.users u WHERE m.name = ?1")
    int countUsersByMemeId(String memeId);

    @Query("SELECT u FROM UserEntity u JOIN u.memes m WHERE m.name = ?1")
    Set<UserEntity> findUsersByMemeName(String memeName);
}
