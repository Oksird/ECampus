package ua.foxminded.muzychenko.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {
        super("Course was not found");
    }
}
