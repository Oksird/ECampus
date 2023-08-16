package ua.foxminded.muzychenko.dao.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User not found");
    }
}
