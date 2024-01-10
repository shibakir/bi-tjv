package shibakir.project.business;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shibakir.project.api.dto.MemeDTO;
import shibakir.project.api.exceptions.EntityStateException;
import shibakir.project.dao.UserJpaRepository;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void checkMethod_Exists() {
        UserEntity user = new UserEntity("username", "password");
        when(userJpaRepository.existsById(user.getUsername())).thenReturn(true);

        boolean exists = userService.exists(user);
        assertTrue(exists);
        verify(userJpaRepository).existsById(user.getUsername());
    }

    @Test
    public void checkMethod_Create() throws EntityStateException {
        UserEntity user = new UserEntity("username", "password");
        when(userJpaRepository.save(user)).thenReturn(user);

        UserEntity createdUser = userService.create(user);

        assertNotNull(createdUser);
        verify(userJpaRepository).save(user);
    }

    @Test
    void checkMethod_Update() throws EntityStateException {
        UserEntity user = new UserEntity("username", "password");
        when(userJpaRepository.save(user)).thenReturn(user);
        UserEntity createdUser = userService.create(user);
        assertNotNull(createdUser);

        when(userJpaRepository.existsById(user.getUsername())).thenReturn(true);

        when(userJpaRepository.existsById(user.getUsername())).thenReturn(false);
        assertThrows(EntityStateException.class, () -> userService.update(user));

        verify(userJpaRepository).save(user);
        verify(userJpaRepository, times(2)).existsById(user.getUsername());
    }

    @Test
    public void checkMethod_DeleteById() {
        String userId = "username";
        UserEntity user = new UserEntity(userId, "password");
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteById(userId);
        verify(userJpaRepository).deleteById(userId);
    }

    @Test
    public void checkMethod_getMemesOfUser() {
        String username = "username";
        Set<MemeEntity> memeEntities = Stream.of(new MemeEntity(), new MemeEntity()).collect(Collectors.toSet());
        when(userJpaRepository.findMemesByUsername(username)).thenReturn(memeEntities);
        when(userJpaRepository.findMemesByUsername("nonexistentUser")).thenReturn(Collections.emptySet());

        List<MemeDTO> returnedMemes = userService.getMemesOfUser(username);

        assertEquals(returnedMemes.size(), memeEntities.size());
        assertTrue(userService.getMemesOfUser("nonexistentUser").isEmpty());
    }
}
