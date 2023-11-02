package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.StaffProfile;
import ua.foxminded.muzychenko.entity.Staff;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = StaffProfileMapper.class)
class StaffProfileMapperTest {

    @Autowired
    private StaffProfileMapper mapper;

    @Test
    void mapStaffEntityToProfileShouldReturnStaffProfile() {

        Staff staff = new Staff(
            UUID.randomUUID(),
            "name",
            "last name",
            "email",
            "pass"
        );

        StaffProfile expectedStaffProfile = new StaffProfile(
            staff.getUserId().toString(),
            staff.getFirstName(),
            staff.getLastName(),
            staff.getEmail()
        );

        assertEquals(expectedStaffProfile, mapper.mapStaffEntityToProfile(staff));
    }
}