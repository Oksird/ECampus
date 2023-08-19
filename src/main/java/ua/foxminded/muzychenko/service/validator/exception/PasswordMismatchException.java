package ua.foxminded.muzychenko.service.validator.exception;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException() {
        super("Password do not match");
    }
}
