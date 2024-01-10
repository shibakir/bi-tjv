package shibakir.project.business;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import shibakir.project.api.converter.UserConverter;
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
public class MemeService extends AbstractCrudService<String, MemeEntity> {

    private final MemeJpaRepository memeJpaRepository;
    private final UserJpaRepository userJpaRepository;
    @Override
    public boolean exists(MemeEntity memeEntity) {
        return repository.existsById(memeEntity.getName());
    }

    public MemeService(MemeJpaRepository memeJpaRepository, UserJpaRepository userJpaRepository) {
        super(memeJpaRepository);
        this.memeJpaRepository = memeJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    public int getUserCountForMeme(String memeId) {
        return memeJpaRepository.countUsersByMemeId(memeId);
    }

    @Override
    public MemeEntity create(MemeEntity entity) throws EntityStateException {
        entity.setName(entity.getName());
        return super.create(entity);
    }

    @Override
    public MemeEntity update(MemeEntity entity) throws EntityStateException {
        if (!exists(entity)) {
            throw new EntityStateException();
        }
        return super.update(entity);
    }

    @Transactional
    public void addUserToMeme(String memeId, String userId) throws EntityStateException {
        MemeEntity memeEntity = repository.findById(memeId)
                .orElseThrow(() -> new EntityStateException("Meme not found"));
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityStateException("User not found"));

        memeEntity.getUsers().add(userEntity);
        repository.save(memeEntity);
    }

    @Override
    public void deleteById(String memeId) {
        MemeEntity meme = memeJpaRepository.findById(memeId).orElse(null);

        if(meme != null) {
            if(!meme.getUsers().isEmpty()) {
                for (UserEntity user : new ArrayList<>(meme.getUsers())){
                    user.getMemes().remove(meme);
                }
            }
            memeJpaRepository.deleteById(memeId);
        }
    }

    public List<UserDTO> getUsersForMeme(String memeName) {
        Set<UserEntity> userEntities = memeJpaRepository.findUsersByMemeName(memeName);
        return userEntities.stream()
                .map(UserConverter::fromModel)
                .collect(Collectors.toList());
    }
}
