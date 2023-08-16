package ua.foxminded.muzychenko.dao.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {
        super("Course was not found");
    }
}
