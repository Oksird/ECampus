package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.StaffProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.Staff;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.StaffRepository;
import ua.foxminded.muzychenko.service.mapper.StaffProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = StaffService.class)
class StaffServiceTest {
    @MockBean
    private StaffRepository staffRepository;
    @MockBean
    private PendingUserRepository pendingUserRepository;
    @MockBean
    private RequestValidator passwordValidator;
    @MockBean
    private StaffProfileMapper staffProfileMapper;
    @Autowired
    private StaffService staffService;

    @Test
    void createStaffFromPendingUserShouldCreateStaffFromPendingUser() {

        UUID id = UUID.randomUUID();

        PendingUser pendingUser = new PendingUser(
            id,
            "fn",
            "ln",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        Staff staff = new Staff(
            id,
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPassword(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        PendingUserProfile pendingUserProfile = new PendingUserProfile(
            pendingUser.getUserId().toString(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        when(pendingUserRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(pendingUser));
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);

        doNothing().when(pendingUserRepository).delete(pendingUser);

        staffService.createStaffFromPendingUser(pendingUserProfile);

        verify(pendingUserRepository).findById(any(UUID.class));
        verify(pendingUserRepository).delete(pendingUser);
        assertEquals(staff, staffRepository.save(staff));
    }

    @Test
    void findStaffByIdShouldReturnStaffWithCorrectID() {
        Staff staff = new Staff(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        StaffProfile staffProfile = new StaffProfile(
            staff.getUserId().toString(),
            staff.getFirstName(),
            staff.getLastName(),
            staff.getEmail(),
            staff.getPhoneNumber(),
            staff.getAddress()
        );

        when(staffRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(staff));

        when(staffProfileMapper.mapStaffEntityToProfile(any(Staff.class)))
            .thenReturn(staffProfile);

        assertEquals(staffProfile, staffService.findStaffById(UUID.randomUUID()));
    }

    @Test
    void findAllStaffShouldReturnListOfAllStaffByPage() {
        List<Staff> staffList = new ArrayList<>(List.of(
            new Staff(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass",
                "380227738888",
                "address"
            ),
            new Staff(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                "380227738888",
                "address"
            )
        ));

        Staff staff1 = staffList.get(0);
        Staff staff2 = staffList.get(1);

        StaffProfile staffProfile1 = new StaffProfile(
            staff1.getUserId().toString(),
            staff1.getFirstName(),
            staff1.getLastName(),
            staff1.getEmail(),
            staff1.getPhoneNumber(),
            staff1.getAddress()
        );

        StaffProfile staffProfile2 = new StaffProfile(
            staff2.getUserId().toString(),
            staff2.getFirstName(),
            staff2.getLastName(),
            staff2.getEmail(),
            staff2.getPhoneNumber(),
            staff2.getAddress()
        );

        List<StaffProfile> staffProfiles = new ArrayList<>(List.of(staffProfile1, staffProfile2));

        when(staffProfileMapper.mapStaffEntityToProfile(staff1))
            .thenReturn(staffProfile1);
        when(staffProfileMapper.mapStaffEntityToProfile(staff2))
            .thenReturn(staffProfile2);

        Page<Staff> expectedPage = new PageImpl<>(staffList);

        when(staffRepository.findAll(any(Pageable.class)))
            .thenReturn(expectedPage);

        assertEquals(staffProfiles, staffService.findAll(1,1).getContent());
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfStaffInDataBase() {
        Staff staff = new Staff(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        when(staffRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(staff));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        when(staffRepository.save(any(Staff.class)))
            .thenReturn(staff);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        staffService.changePassword(passwordChangeRequest);

        verify(staffRepository).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(staffRepository).save(any(Staff.class));
    }

    @Test
    void deleteStaffShouldRemoveStaffFromDataBase() {
        Staff staff = new Staff(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        when(staffRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(staff));

        doNothing()
            .when(staffRepository)
            .deleteById(any(UUID.class));

        staffService.deleteStaff(staff.getUserId());

        verify(staffRepository).deleteById(staff.getUserId());
    }

    @Test
    void findStaffByEmailShouldReturnCorrectStaffProfile() {
        Staff staff = new Staff(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pas",
            "380227738888",
            "address"
        );

        StaffProfile staffProfile = new StaffProfile(
            staff.getUserId().toString(),
            staff.getFirstName(),
            staff.getLastName(),
            staff.getEmail(),
            staff.getPhoneNumber(),
            staff.getAddress()
        );

        when(staffRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(staff));
        when(staffProfileMapper.mapStaffEntityToProfile(staff))
            .thenReturn(staffProfile);

        assertEquals(staffProfile, staffService.findStaffByEmail("email"));
    }
}
