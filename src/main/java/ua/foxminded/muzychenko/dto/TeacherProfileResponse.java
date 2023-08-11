package ua.foxminded.muzychenko.dto;

import lombok.Getter;
import ua.foxminded.muzychenko.entity.Course;

import java.util.List;

@Getter
public class TeacherProfileResponse extends UserProfileResponse {
    private final List<Course> courses;

    public TeacherProfileResponse(String firstName, String lastName, String email, List<Course> courses) {
        super(firstName, lastName, email);
        this.courses = courses;
    }
}
