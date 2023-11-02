package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = PendingUserRepository.class)
@Import(DataTestConfig.class)
@Transactional
class PendingUserRepositoryTest {

    @Autowired
    private PendingUserRepository pendingUserRepository;

    @Test
    void findByEmailShouldReturnOptionalOfPendingUserWithCorrectEmail() {
        String email = "eps1@gmail.com";

        PendingUser expectedUser = new PendingUser(
            UUID.randomUUID(),
            "Lee",
            "Martinez",
            email,
            "student101"

        );
        PendingUser actualUser = pendingUserRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        expectedUser.setUserId(actualUser.getUserId());

        assertEquals(expectedUser, actualUser);
    }
}
