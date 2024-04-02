package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString
public class TeacherProfile extends AbstractUserProfile {
    public TeacherProfile(String userId , String firstName, String lastName, String email,
                          String phoneNumber, String address) {
        super(userId ,firstName, lastName, email, phoneNumber, address);
    }
}
