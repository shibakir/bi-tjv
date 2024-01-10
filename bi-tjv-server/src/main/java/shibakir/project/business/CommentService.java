package shibakir.project.business;

import org.springframework.stereotype.Component;
import shibakir.project.api.exceptions.EntityStateException;
import shibakir.project.dao.CommentJpaRepository;
import shibakir.project.domain.CommentEntity;

@Component
public class CommentService extends AbstractCrudService<Long, CommentEntity> {
    @Override
    public boolean exists(CommentEntity commentEntity) {
        return false;
    }

    public CommentService(CommentJpaRepository commentJpaRepository) {
        super(commentJpaRepository);
    }

    @Override
    public CommentEntity create(CommentEntity entity) throws EntityStateException {
        entity.setId(entity.getId());
        return super.create(entity);
    }

    @Override
    public CommentEntity update(CommentEntity entity) throws EntityStateException {
        if (!exists(entity)) {
            throw new EntityStateException();
        }
        return super.update(entity);
    }
}
