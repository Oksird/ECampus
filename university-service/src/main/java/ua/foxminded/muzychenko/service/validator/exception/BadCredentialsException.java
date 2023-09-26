package ua.foxminded.muzychenko.service.validator.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException() {
        super("Bad credentials");
    }
}
