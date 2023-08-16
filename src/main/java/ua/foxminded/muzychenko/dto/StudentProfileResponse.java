package ua.foxminded.muzychenko.dto;

import lombok.Getter;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;

import java.util.List;

@Getter
public class StudentProfileResponse extends UserProfileResponse {
    private final Group group;
    private final List<Course> courses;

    public StudentProfileResponse(String firstName, String lastName, String email, Group group, List<Course> courses) {
        super(firstName, lastName, email);
        this.group = group;
        this.courses = courses;
    }
}
