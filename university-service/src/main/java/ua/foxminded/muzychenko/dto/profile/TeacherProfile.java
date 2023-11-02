package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class TeacherProfile extends AbstractUserProfile {
    private Set<CourseInfo> coursesInfo;
    public TeacherProfile(String userId , String firstName, String lastName, String email, Set<CourseInfo> coursesInfo) {
        super(userId ,firstName, lastName, email);
        this.coursesInfo = coursesInfo;
    }
}
