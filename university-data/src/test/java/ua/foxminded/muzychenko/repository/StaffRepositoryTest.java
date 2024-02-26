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
@SpringBootTest(classes = StaffRepository.class)
@Import(DataTestConfig.class)
@Transactional
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    @Test
    void findByEmailShouldReturnOptionalOfPendingUserWithCorrectEmail() {
        String email = "zemail@mail.com";

        Staff expectedUser = new Staff(
            UUID.randomUUID(),
            "Lim",
            "Simos",
            email,
            "password10",
            "380786623455",
            "Tasmowa 99"
        );

        Staff actualUser = staffRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        expectedUser.setUserId(actualUser.getUserId());

        assertEquals(expectedUser, actualUser);
    }
}
