package shibakir.project.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import shibakir.project.api.exceptions.*;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<Key, Entity> {

    protected final JpaRepository<Entity, Key> repository;

    protected AbstractCrudService(JpaRepository<Entity, Key> repository) {
        this.repository = repository;
    }

    @Transactional
    public Entity create(Entity entity) throws EntityStateException {
        if (exists(entity))
            throw new EntityStateException(entity);
        return repository.save(entity);
    }

    public abstract boolean exists(Entity entity);

    public Optional<Entity> readById(Key id) {
        return repository.findById(id);
    }

    public List<Entity> readAll() {
        return repository.findAll();
    }

    @Transactional
    public Entity update(Entity entity) throws EntityStateException {
        if (exists(entity))
            return repository.save(entity);
        else
            throw new EntityStateException(entity);
    }

    public void deleteById(Key id) throws HasRelationException {
        repository.deleteById(id);
    }

}