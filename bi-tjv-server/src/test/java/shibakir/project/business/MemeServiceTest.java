package shibakir.project.business;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import shibakir.project.api.dto.UserDTO;
import shibakir.project.api.exceptions.EntityStateException;
import shibakir.project.dao.MemeJpaRepository;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class MemeServiceTest {

    @Mock
    private MemeJpaRepository memeJpaRepository;

    @InjectMocks
    private MemeService memeService;

    @Test
    public void checkMethod_Exists() {
        MemeEntity meme = new MemeEntity("name", "text");
        when(memeJpaRepository.existsById(meme.getName())).thenReturn(true);

        boolean exists = memeService.exists(meme);
        assertTrue(exists);
        verify(memeJpaRepository).existsById(meme.getName());
    }

    @Test
    public void checkMethod_Create() throws EntityStateException {
        MemeEntity meme = new MemeEntity("name", "text");
        when(memeJpaRepository.save(meme)).thenReturn(meme);

        MemeEntity createdMeme = memeService.create(meme);

        assertNotNull(createdMeme);
        verify(memeJpaRepository).save(meme);
    }

    @Test
    void checkMethod_Update() throws EntityStateException {
        MemeEntity meme = new MemeEntity("name", "text");
        when(memeJpaRepository.save(meme)).thenReturn(meme);
        MemeEntity createdMeme = memeService.create(meme);
        assertNotNull(createdMeme);

        when(memeJpaRepository.existsById(meme.getName())).thenReturn(true);
        when(memeJpaRepository.existsById(meme.getName())).thenReturn(false);
        assertThrows(EntityStateException.class, () -> memeService.update(meme));

        verify(memeJpaRepository).save(meme);
        verify(memeJpaRepository, times(2)).existsById(meme.getName());
    }

    @Test
    public void checkMethod_DeleteById() {
        String memeId = "name";
        MemeEntity meme = new MemeEntity(memeId, "text");
        when(memeJpaRepository.findById(memeId)).thenReturn(Optional.of(meme));

        memeService.deleteById(memeId);

        verify(memeJpaRepository).deleteById(memeId);
    }

    @Test
    public void checkMethod_getUsersForMeme() {
        String memeId = "name";
        Set<UserEntity> userEntities = Stream.of(new UserEntity(), new UserEntity()).collect(Collectors.toSet());
        when(memeJpaRepository.findUsersByMemeName(memeId)).thenReturn(userEntities);
        when(memeJpaRepository.findUsersByMemeName("nonexistentMeme")).thenReturn(Collections.emptySet());

        List<UserDTO> returnedUsers = memeService.getUsersForMeme(memeId);

        assertEquals(returnedUsers.size(), userEntities.size());
        assertTrue(memeService.getUsersForMeme("nonexistentMeme").isEmpty());
    }
}
