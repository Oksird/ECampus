package ua.foxminded.muzychenko.university.service.validator.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException() {
        super("Bad credentials");
    }
}
