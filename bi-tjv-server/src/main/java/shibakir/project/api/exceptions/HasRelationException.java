package shibakir.project.api.exceptions;

public class HasRelationException extends Exception {
    public HasRelationException() {
    }

    public <E> HasRelationException(E entity) {
        super("Instance has relation in other instance" + entity);
    }
}
