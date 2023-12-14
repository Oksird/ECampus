package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class TeacherProfile extends AbstractUserProfile {
    public TeacherProfile(String userId , String firstName, String lastName, String email,
                          String phoneNumber, String address) {
        super(userId ,firstName, lastName, email, phoneNumber, address);
    }
}
