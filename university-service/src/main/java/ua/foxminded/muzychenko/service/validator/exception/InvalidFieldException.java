package ua.foxminded.muzychenko.service.validator.exception;

public class InvalidFieldException extends RuntimeException{
    public InvalidFieldException(String field) {
        super("Invalid " + field);
    }
}
