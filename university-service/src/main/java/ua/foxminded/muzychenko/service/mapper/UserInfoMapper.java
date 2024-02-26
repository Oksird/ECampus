package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.UserInfo;
import ua.foxminded.muzychenko.entity.AbstractUser;

@Mapper
public class UserInfoMapper {
    public UserInfo mapUserToDTO(AbstractUser abstractUser) {
        return new UserInfo(
            abstractUser.getUserId().toString(),
            abstractUser.getEmail(),
            abstractUser.getFirstName(),
            abstractUser.getLastName(),
            abstractUser.getPhoneNumber(),
            abstractUser.getRole(),
            abstractUser.getAddress()
        );
    }
}
