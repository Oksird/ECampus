package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.entity.Admin;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AdminProfileMapper.class)
class AdminProfileMapperTest {

    @Autowired
    private AdminProfileMapper mapper;

    @Test
    void mapAdminEntityToAdminProfileShouldReturnAdminProfileBasedOnAdminEntity() {

        Admin admin = new Admin(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380773216622",
            "address"
        );

        AdminProfile expectedAdminProfile = new AdminProfile(
            admin.getUserId().toString(),
            admin.getFirstName(),
            admin.getLastName(),
            admin.getEmail(),
            admin.getPhoneNumber(),
            admin.getAddress()
        );

        assertEquals(expectedAdminProfile, mapper.mapAdminEntityToAdminProfile(admin));
    }
}
