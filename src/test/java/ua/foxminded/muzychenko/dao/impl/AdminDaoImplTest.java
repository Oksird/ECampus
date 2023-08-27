package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.entity.Admin;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class AdminDaoImplTest {

    @Autowired
    private AdminDao adminDao;

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
        adminDao.create(admin);
        assertEquals(admin, adminDao.findById(admin.getUserId()).orElse(null));
    }

    @DisplayName("Admin was updated")
    @Test
    void updateShouldReplaceTeacher() {
        Admin oldAdmin = adminDao.findAll().get(0);
        Admin admin = new Admin(
            oldAdmin.getUserId(),
            "name",
            "surname",
            "mail",
            "pass"
        );
        adminDao.update(oldAdmin.getUserId(), admin);
        assertEquals(admin, adminDao.findById(oldAdmin.getUserId()).orElse(null));
    }

    @DisplayName("Thrown exception when created admin is null")
    @Test
    void createShouldThrowExceptionWhenAdminIsNull() {
        assertThrows(NullPointerException.class, () -> adminDao.create(null));
    }

    @DisplayName("Updated admin cant replaced with null")
    @Test
    void updateShouldThrowExceptionWhenNewUserIsNull() {
        Admin oldAdmin = adminDao.findAll().get(0);
        UUID oldId = oldAdmin.getUserId();
        assertThrows(NullPointerException.class, () -> adminDao.update(oldId, null));
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
        Admin actualAdmin = adminDao.findByEmail("john.smith@example.com").orElse(null);
        assert actualAdmin != null;
        expectedAdmin.setUserId(actualAdmin.getUserId());
        assertEquals(expectedAdmin, actualAdmin);
    }
}