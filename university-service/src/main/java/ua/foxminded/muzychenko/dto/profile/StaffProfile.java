package ua.foxminded.muzychenko.dto.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class StaffProfile extends AbstractUserProfile {
    public StaffProfile(String userId, String firstName, String lastName,
                        String email, String phoneNumber, String address) {
        super(userId, firstName, lastName, email, phoneNumber, address);
    }
}
