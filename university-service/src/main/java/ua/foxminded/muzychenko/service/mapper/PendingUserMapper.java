package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.entity.PendingUser;

@Mapper
public class PendingUserMapper {
    public PendingUserProfile mapPendingUserEntityToProfile(PendingUser pendingUser) {
        return new PendingUserProfile(
            pendingUser.getUserId().toString(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail()
        );
    }
}
