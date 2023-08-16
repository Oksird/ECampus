package ua.foxminded.muzychenko.dao.exception;

public class EntityWasNotFoundException extends RuntimeException {
    public EntityWasNotFoundException() {
        super("Entity was not found");
    }
}
