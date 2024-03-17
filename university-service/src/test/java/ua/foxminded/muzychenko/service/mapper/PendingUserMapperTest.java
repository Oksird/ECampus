package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.entity.PendingUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = PendingUserMapper.class)
class PendingUserMapperTest {

    @Autowired
    private PendingUserMapper mapper;

    @Test
    void mapPendingUserEntityToProfileShouldReturnPendingUserProfile() {

        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "name",
            "last name",
            "email",
            "pass",
            "123123123",
            "address"
        );

        PendingUserProfile expectedPendingUserProfile = new PendingUserProfile(
            pendingUser.getUserId().toString(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        assertEquals(expectedPendingUserProfile, mapper.mapPendingUserEntityToProfile(pendingUser));
    }
}
