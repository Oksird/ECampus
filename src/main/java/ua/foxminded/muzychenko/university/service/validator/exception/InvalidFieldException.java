package ua.foxminded.muzychenko.university.service.validator.exception;

public class InvalidFieldException extends RuntimeException{
    public InvalidFieldException(String field) {
        super("Invalid " + field);
    }
}
