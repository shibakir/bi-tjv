package shibakir.project.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shibakir.project.dao.UserJpaRepository;
import shibakir.project.domain.UserEntity;
import shibakir.project.domain.MemeEntity;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private MemeJpaRepository memeJpaRepository;

    @Test
    public void checkMethod_FindUsersNotLinkedToMeme() {
        UserEntity user1 = new UserEntity("username1", "password1");
        UserEntity user2 = new UserEntity("username2", "password2");
        MemeEntity meme = new MemeEntity("name", "text");

        userJpaRepository.save(user1);
        userJpaRepository.save(user2);
        memeJpaRepository.save(meme);
        meme.getUsers().add(user1);
        memeJpaRepository.save(meme);

        List<UserEntity> users = userJpaRepository.findUsersNotLinkedToMeme("name");
        assertTrue(users.contains(user2));
        assertFalse(users.contains(user1));
    }
}
