package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.UserInfo;
import ua.foxminded.muzychenko.entity.AbstractUser;
import ua.foxminded.muzychenko.entity.Admin;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = UserInfoMapper.class)
public class UserInfoMapperTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    void mapUserToDTOShouldMapEntity() {
        AbstractUser user = new Admin(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "pn",
            "adr"
        );

        UserInfo userInfo = new UserInfo(
            user.getUserId().toString(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getRole(),
            user.getAddress()
        );

        assertEquals(userInfo, userInfoMapper.mapUserToDTO(user));
    }
}
