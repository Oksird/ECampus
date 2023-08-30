package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.AdminRepository;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.service.mapper.AdminProfileMapper;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
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

@SpringJUnitConfig(TestConfig.class)
class AdminServiceTest {
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private PasswordValidator passwordValidator;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AdminProfileMapper adminProfileMapper;
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

        AdminProfile adminProfile = new AdminProfile(
          admin.getFirstName(),
          admin.getLastName(),
          admin.getEmail()
        );

        when(adminRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(admin));

        when(adminProfileMapper.mapAdminEntityToAdminProfile(any(Admin.class)))
            .thenReturn(adminProfile);

        assertEquals(adminProfile, adminService.findAdminById(UUID.randomUUID()));
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

        Admin admin1 = adminList.get(0);
        Admin admin2 = adminList.get(1);

        AdminProfile adminProfile1 = new AdminProfile(admin1.getFirstName(), admin1.getLastName(), admin1.getEmail());
        AdminProfile adminProfile2 = new AdminProfile(admin2.getFirstName(), admin2.getLastName(), admin2.getEmail());

        List<AdminProfile> adminProfileList = new ArrayList<>(List.of(adminProfile1, adminProfile2));

        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin1))
            .thenReturn(adminProfile1);
        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin2))
            .thenReturn(adminProfile2);

        Page<Admin> expectedPage = new PageImpl<>(adminList);

        when(adminRepository.findAll(any(Pageable.class)))
                .thenReturn(expectedPage);

        assertEquals(adminProfileList, adminService.findAllAdmins(1,1));
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

        when(adminRepository.save(any(Admin.class)))
                .thenReturn(admin);

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

        when(adminRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        AdminProfile expectedAdminProfile = new AdminProfile(
            admin.getFirstName(),
            admin.getLastName(),
            admin.getEmail()
        );

        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin))
            .thenReturn(expectedAdminProfile);

        UserLoginRequest userLoginRequest = new UserLoginRequest(
            admin.getEmail(),
            admin.getPassword()
        );

        assertEquals(expectedAdminProfile, adminService.login(userLoginRequest));
    }

    @Test
    void registerShouldCreateNewAdminEntityInDataBase() {
        Admin admin = new Admin(UUID.randomUUID(), "fn", "ln", "em", "pass");

        doNothing()
            .when(requestValidator).
            validateUserRegistrationRequest(any(UserRegistrationRequest.class));

        when(adminRepository.save(any(Admin.class)))
                .thenReturn(admin);

        when(passwordEncoder.encode("pass"))
            .thenReturn("encodedPassword");


        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email",
            "pass",
            "pass",
            "fn",
            "ln"
        );

        adminService.register(userRegistrationRequest);

        verify(passwordEncoder).encode(any(String.class));
        verify(requestValidator).validateUserRegistrationRequest(userRegistrationRequest);
        verify(adminRepository).save(any(Admin.class));
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

        when(adminRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        when(adminRepository.save(any(Admin.class)))
                .thenReturn(admin);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        adminService.changePassword(passwordChangeRequest);

        verify(adminRepository).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(adminRepository).save(any(Admin.class));
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

        when(adminRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        doNothing()
            .when(adminRepository)
            .deleteById(any(UUID.class));

        adminService.deleteAdmin(admin.getEmail());

        verify(adminRepository).findByEmail(admin.getEmail());
        verify(adminRepository).deleteById(admin.getUserId());
    }

    @Test
    void findAdminByEmailShouldReturnCorrectAdminProfile() {
        Admin admin = new Admin(UUID.randomUUID(), "fn", "ln", "em", "pas");
        AdminProfile adminProfile = new AdminProfile(admin.getFirstName(), admin.getLastName(), admin.getEmail());
        when(adminRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));
        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin))
            .thenReturn(adminProfile);

        assertEquals(adminProfile, adminService.findAdminByEmail("email"));
    }
}