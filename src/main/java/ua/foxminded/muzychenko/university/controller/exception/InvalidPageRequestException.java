package ua.foxminded.muzychenko.university.controller.exception;

public class InvalidPageRequestException extends RuntimeException {
    public InvalidPageRequestException() {
        super("Invalid page request");
    }
}
