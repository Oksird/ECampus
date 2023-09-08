package ua.foxminded.muzychenko.university.dto.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
public class TeacherProfile extends UserProfile {
    private Set<CourseInfo> coursesInfo;
    public TeacherProfile(String firstName, String lastName, String email, Set<CourseInfo> coursesInfo) {
        super(firstName, lastName, email);
        this.coursesInfo = coursesInfo;
    }
}
