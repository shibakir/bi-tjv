package shibakir.project.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemeJpaRepositoryTest {

    @Autowired
    private MemeJpaRepository memeJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    public void checkMethod_CountUsersByMemeId() {
        UserEntity user = new UserEntity("username", "password");
        MemeEntity meme = new MemeEntity("name", "text");

        memeJpaRepository.save(meme);
        userJpaRepository.save(user);
        meme.getUsers().add(user);
        memeJpaRepository.save(meme);

        int count = memeJpaRepository.countUsersByMemeId("name");
        assertEquals(1, count);
    }

    @Test
    public void checkMethod_FindUsersByMemeName() {
        UserEntity user = new UserEntity("username", "password");
        MemeEntity meme = new MemeEntity("name", "text");

        memeJpaRepository.save(meme);
        userJpaRepository.save(user);
        meme.getUsers().add(user);
        memeJpaRepository.save(meme);

        Set<UserEntity> users = memeJpaRepository.findUsersByMemeName("name");
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }
}
