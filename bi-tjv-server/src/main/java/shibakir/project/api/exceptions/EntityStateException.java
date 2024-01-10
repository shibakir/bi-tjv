package shibakir.project.api.exceptions;

public class EntityStateException extends Exception {
    public EntityStateException() {
    }

    public <E> EntityStateException(E entity) {
        super("Illegal state of entity " + entity);
    }
}
