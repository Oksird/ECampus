package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Admin;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = AdminRepository.class)
@Import(DataTestConfig.class)
@Transactional
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("Admin was created")
    @Test
    void createShouldCreateNewAdmin() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "name",
            "surname",
            "emailA",
            "pass"
        );
        adminRepository.save(admin);
        assertEquals(admin, adminRepository.findById(admin.getUserId()).orElse(null));
    }

    @DisplayName("Admin was updated")
    @Test
    void updateShouldReplaceTeacher() {
        Admin oldAdmin = adminRepository.findAll().get(0);
        Admin admin = new Admin(
            oldAdmin.getUserId(),
            "name",
            "surname",
            "mail",
            "pass"
        );
        adminRepository.save(admin);
        assertEquals(admin, adminRepository.findById(oldAdmin.getUserId()).orElse(null));
    }

    @DisplayName("Thrown exception when created admin is null")
    @Test
    void createShouldThrowExceptionWhenAdminIsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> adminRepository.save(null));
    }

    @DisplayName("Updated admin cant replaced with null")
    @Test
    void updateShouldThrowExceptionWhenNewUserIsNull() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> adminRepository.save(null));
    }

    @DisplayName("Admin is found by email")
    @Test
    void findByEmailShouldReturnAdminIfEmailIsCorrect() {
        Admin expectedAdmin = new Admin(
            UUID.randomUUID(),
            "John",
            "Smith",
            "john.smith@example.com",
            "admin1_password"
        );
        Admin actualAdmin = adminRepository.findByEmail("john.smith@example.com").orElse(null);
        assert actualAdmin != null;
        expectedAdmin.setUserId(actualAdmin.getUserId());
        assertEquals(expectedAdmin, actualAdmin);
    }
}