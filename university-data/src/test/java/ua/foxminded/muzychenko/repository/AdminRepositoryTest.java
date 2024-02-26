package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = AdminRepository.class)
@Import(DataTestConfig.class)
@Transactional
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("Admin is found by email")
    @Test
    void findByEmailShouldReturnAdminIfEmailIsCorrect() {
        String email = "kemail@mail.com";

        System.out.println(adminRepository.findAll().size());

        Admin expectedAdmin = new Admin(
            UUID.randomUUID(),
            "Jurich",
            "Lomonov",
            email,
            "password8",
            "380316457755",
            "Markosh 9"
        );

        Admin actualAdmin = adminRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        expectedAdmin.setUserId(actualAdmin.getUserId());

        assertEquals(expectedAdmin, actualAdmin);
    }
}