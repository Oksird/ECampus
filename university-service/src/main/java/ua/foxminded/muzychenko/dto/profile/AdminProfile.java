package ua.foxminded.muzychenko.dto.profile;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class AdminProfile extends UserProfile {
    public AdminProfile(String userId ,String firstName, String lastName, String email) {
        super(userId ,firstName, lastName, email);
    }
}
