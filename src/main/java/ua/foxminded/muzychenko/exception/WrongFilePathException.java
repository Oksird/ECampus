package ua.foxminded.muzychenko.exception;

public class WrongFilePathException extends RuntimeException {

    public WrongFilePathException() {

    }

    public WrongFilePathException(String message) {
        super(message);
    }
}
