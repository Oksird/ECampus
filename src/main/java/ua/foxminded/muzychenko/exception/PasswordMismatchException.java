package ua.foxminded.muzychenko.exception;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException() {
        super("Password do not match");
    }
}
