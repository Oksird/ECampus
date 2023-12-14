package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AdminProfile extends AbstractUserProfile {
    public AdminProfile(String userId , String firstName, String lastName,
                        String email, String phoneNumber, String address) {
        super(userId ,firstName, lastName, email, phoneNumber, address);
    }
}
