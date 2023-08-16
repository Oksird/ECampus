package ua.foxminded.muzychenko.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherProfile extends UserProfile {
    private List<CourseInfo> coursesInfo;
    public TeacherProfile(String firstName, String lastName, String email, List<CourseInfo> coursesInfo) {
        super(firstName, lastName, email);
        this.coursesInfo = coursesInfo;
    }
}
