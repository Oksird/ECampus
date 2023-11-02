package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Staff;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = PendingUserRepository.class)
@Import(DataTestConfig.class)
@Transactional
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    @Test
    void findByEmailShouldReturnOptionalOfPendingUserWithCorrectEmail() {
        String email = "esf1@gmail.com";

        Staff expectedUser = new Staff(
            UUID.randomUUID(),
            "Max",
            "Pop",
            email,
            "student1223"

        );
        Staff actualUser = staffRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        expectedUser.setUserId(actualUser.getUserId());

        assertEquals(expectedUser, actualUser);
    }
}
