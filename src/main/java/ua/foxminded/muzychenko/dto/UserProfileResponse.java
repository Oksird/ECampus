package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Group group;
    private List<Course> courses;
}
