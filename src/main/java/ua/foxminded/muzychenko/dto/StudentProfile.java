package ua.foxminded.muzychenko.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StudentProfile extends UserProfile {
    private GroupInfo groupInfo;
    private List<CourseInfo> coursesInfo;
    public StudentProfile(String firstName, String lastName, String email, GroupInfo groupInfo, List<CourseInfo> coursesInfo) {
        super(firstName, lastName, email);
        this.groupInfo = groupInfo;
        this.coursesInfo = coursesInfo;
    }
}
