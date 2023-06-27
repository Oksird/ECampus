package ua.foxminded.muzychenko.exception;

public class DataBaseRunTimeException extends RuntimeException {

    public DataBaseRunTimeException(String message, Throwable clause) {
        super(message, clause);
    }

    public DataBaseRunTimeException(Throwable clause) {
        super(clause);
    }
}
