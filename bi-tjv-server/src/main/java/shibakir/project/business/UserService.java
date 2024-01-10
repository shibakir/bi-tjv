package shibakir.project.business;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import shibakir.project.api.converter.MemeConverter;
import shibakir.project.api.converter.UserConverter;
import shibakir.project.api.dto.MemeDTO;
import shibakir.project.api.dto.UserDTO;
import shibakir.project.api.exceptions.EntityStateException;
import shibakir.project.dao.MemeJpaRepository;
import shibakir.project.dao.UserJpaRepository;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserService extends AbstractCrudService<String, UserEntity> {

    private final UserJpaRepository userJpaRepository;
    private final MemeJpaRepository memeJpaRepository;

    @Override
    public boolean exists(UserEntity entity) {
        return repository.existsById(entity.getUsername());
    }

    public UserService(UserJpaRepository userJpaRepository, MemeJpaRepository memeJpaRepository) {
        super(userJpaRepository);
        this.userJpaRepository = userJpaRepository;
        this.memeJpaRepository = memeJpaRepository;
    }

    @Override
    public UserEntity create(UserEntity entity) throws EntityStateException {
        entity.setUsername(entity.getUsername());
        return super.create(entity);
    }

    @Override
    public UserEntity update(UserEntity entity) throws EntityStateException {
        if (!exists(entity)) {
            throw new EntityStateException();
        }
        return super.update(entity);
    }

    @Transactional
    public void addMemeToUser(String userId, String memeId) throws EntityStateException {
        MemeEntity memeEntity = memeJpaRepository.findById(memeId)
                .orElseThrow(() -> new EntityStateException("Meme not found"));
        UserEntity userEntity = repository.findById(userId)
                .orElseThrow(() -> new EntityStateException("User not found"));

        userEntity.getMemes().add(memeEntity);
        repository.save(userEntity);
    }

    @Override
    public void deleteById(String userId) {
        UserEntity user = userJpaRepository.findById(userId).orElse(null);
        if (user != null) {
            if (!user.getMemes().isEmpty()) {
                for (MemeEntity meme : new ArrayList<>(user.getMemes())) {
                    meme.getUsers().remove(user);
                }
            }
            userJpaRepository.deleteById(userId);
        }
    }

    public List<UserDTO> getUsersNotLinkedToMeme(String memeName) {
        List<UserEntity> users = userJpaRepository.findUsersNotLinkedToMeme(memeName);
        return users.stream()
                .map(UserConverter::fromModel).toList();
    }

    public List<MemeDTO> getMemesOfUser(String username) {
        Set<MemeEntity> memeEntities = userJpaRepository.findMemesByUsername(username);
        return memeEntities.stream()
                .map(MemeConverter::fromModel)
                .collect(Collectors.toList());
    }
}
