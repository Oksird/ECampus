package ua.foxminded.muzychenko.university.dao.exception;

public class LessonNotFoundException extends RuntimeException{
    public LessonNotFoundException() {
        super("Lesson was not found");
    }
}
