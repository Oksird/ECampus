package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class StudentProfile extends AbstractUserProfile {
    private GroupInfo groupInfo;
    private List<CourseInfo> coursesInfo;
    public StudentProfile(String userId , String firstName, String lastName, String email,
                          GroupInfo groupInfo, String phoneNumber, String address) {
        super(userId ,firstName, lastName, email, phoneNumber, address);
        this.groupInfo = groupInfo;
    }
}
