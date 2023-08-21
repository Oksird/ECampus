package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class AdminServiceTest {
    @MockBean
    private AdminDao adminDao;
    @MockBean
    private PasswordValidator passwordValidator;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminService adminService;

    @Test
    void findAdminByIdShouldReturnAdminWithCorrectID() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        when(adminDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(admin));
        assertEquals(admin, adminService.findAdminById(UUID.randomUUID()));
    }

    @Test
    void findAllAdminsShouldReturnListOfAllAdminsByPage() {
        List<Admin> adminList = new ArrayList<>(List.of(
            new Admin(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass"
            ),
            new Admin(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2"
            )
        ));

        when(adminDao.findAll(any(Long.class), any(Long.class)))
            .thenReturn(adminList);

        assertEquals(adminList, adminService.findAllAdmins(1L,1L));
    }

    @Test
    void loginShouldReturnCorrectAdminProfile() {

        Admin admin = new Admin(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        doNothing()
            .when(adminDao)
            .create(any(Admin.class)
            );

        doNothing()
            .when(passwordValidator)
            .validateEnteredPassword(
                any(String.class),
                any(String.class)
            );

        doNothing()
            .when(requestValidator)
            .validateUserLoginRequest(
                any(UserLoginRequest.class),
                any(String.class),
                any(String.class)
            );

        when(adminDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        AdminProfile expectedAdminProfile = new AdminProfile(
            admin.getFirstName(),
            admin.getLastName(),
            admin.getEmail()
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
            admin.getEmail(),
            admin.getPassword()
        );

        assertEquals(expectedAdminProfile, adminService.login(userLoginRequest));
    }

    @Test
    void registerShouldCreateNewAdminEntityInDataBase() {
        doNothing()
            .when(requestValidator).
            validateUserRegistrationRequest(any(UserRegistrationRequest.class));

        doNothing()
            .when(adminDao)
            .create(any(Admin.class));

        when(passwordEncoder.encode(any(String.class)))
            .thenReturn("encodedString");


        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email",
            "pass",
            "pass",
            "fN",
            "lN"
        );

        adminService.register(userRegistrationRequest);

        verify(passwordEncoder).encode(any(String.class));
        verify(requestValidator).validateUserRegistrationRequest(userRegistrationRequest);
        verify(adminDao).create(any(Admin.class));
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfAdminInDataBase() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(adminDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        doNothing()
            .when(adminDao)
            .update(eq(admin.getUserId()), any(Admin.class));

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        adminService.changePassword(passwordChangeRequest);

        verify(adminDao).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(adminDao).update(eq(admin.getUserId()), any(Admin.class));
    }

    @Test
    void deleteAdminShouldRemoveAdminFromDataBase() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(adminDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        doNothing()
            .when(adminDao)
            .deleteById(any(UUID.class));

        adminService.deleteAdmin(admin.getEmail());

        verify(adminDao).findByEmail(admin.getEmail());
        verify(adminDao).deleteById(admin.getUserId());
    }
}