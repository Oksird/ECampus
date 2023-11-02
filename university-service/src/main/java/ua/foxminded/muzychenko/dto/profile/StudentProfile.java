package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class StudentProfile extends AbstractUserProfile {
    private GroupInfo groupInfo;
    private Set<CourseInfo> coursesInfo;
    public StudentProfile(String userId , String firstName, String lastName, String email, GroupInfo groupInfo, Set<CourseInfo> coursesInfo) {
        super(userId ,firstName, lastName, email);
        this.groupInfo = groupInfo;
        this.coursesInfo = coursesInfo;
    }
}
