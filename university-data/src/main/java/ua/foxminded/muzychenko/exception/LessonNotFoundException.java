package ua.foxminded.muzychenko.exception;

public class LessonNotFoundException extends RuntimeException{
    public LessonNotFoundException() {
        super("Lesson was not found");
    }
}
