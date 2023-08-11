package ua.foxminded.muzychenko.exception;

public class InvalidFieldException extends RuntimeException{
    public InvalidFieldException(String field) {
        super("Invalid " + field);
    }
}
