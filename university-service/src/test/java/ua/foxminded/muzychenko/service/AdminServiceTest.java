package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.RequestStatusDTO;
import ua.foxminded.muzychenko.dto.RequestTypeDTO;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.UserInfo;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.enums.RequestStatusEnum;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;
import ua.foxminded.muzychenko.repository.AdminRepository;
import ua.foxminded.muzychenko.service.mapper.AdminProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = AdminService.class)
class AdminServiceTest {
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private AdminProfileMapper adminProfileMapper;
    @MockBean
    private UserRequestService userRequestService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private StaffService staffService;
    @MockBean
    private PendingUserService pendingUserService;
    @Autowired
    private AdminService adminService;

    @Test
    void findAdminByIdShouldReturnAdminWithCorrectID() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380782238811",
            "address"
        );

        AdminProfile adminProfile = new AdminProfile(
          admin.getUserId().toString(),
          admin.getFirstName(),
          admin.getLastName(),
          admin.getEmail(),
          admin.getPhoneNumber(),
          admin.getAddress()
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
                "pass",
                "380227738888",
                "address"
            ),
            new Admin(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                "380227738888",
                "address"
            )
        ));

        Admin admin1 = adminList.get(0);
        Admin admin2 = adminList.get(1);

        AdminProfile adminProfile1 = new AdminProfile(
            admin1.getUserId().toString(),
            admin1.getFirstName(),
            admin1.getLastName(),
            admin1.getEmail(),
            admin1.getPhoneNumber(),
            admin1.getAddress()
        );

        AdminProfile adminProfile2 = new AdminProfile(
            admin2.getUserId().toString(),
            admin2.getFirstName(),
            admin2.getLastName(),
            admin2.getEmail(),
            admin2.getPhoneNumber(),
            admin2.getAddress()
        );

        List<AdminProfile> adminProfileList = new ArrayList<>(List.of(adminProfile1, adminProfile2));

        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin1))
            .thenReturn(adminProfile1);
        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin2))
            .thenReturn(adminProfile2);

        Page<Admin> expectedPage = new PageImpl<>(adminList);

        when(adminRepository.findAll(any(Pageable.class)))
                .thenReturn(expectedPage);

        assertEquals(adminProfileList, adminService.findAll(1,1).getContent());
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfAdminInDataBase() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        when(adminRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));

        doNothing()
            .when(requestValidator)
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
        verify(requestValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void deleteAdminShouldRemoveAdminFromDataBase() {
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            "380227738888",
            "address"
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
        Admin admin = new Admin(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pas",
            "380227738888",
            "address"
        );

        AdminProfile adminProfile = new AdminProfile(
            admin.getUserId().toString() ,
            admin.getFirstName(),
            admin.getLastName(),
            admin.getEmail(),
            admin.getPhoneNumber(),
            admin.getAddress()
        );

        when(adminRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(admin));
        when(adminProfileMapper.mapAdminEntityToAdminProfile(admin))
            .thenReturn(adminProfile);

        assertEquals(adminProfile, adminService.findAdminByEmail("email"));
    }

    @Test
    void grantRoleOnRequestShouldGrantCorrectRoleByRequest() {
        String email = "em@mail.com";
        String firstName = "Name";
        String lastName = "Surname";
        String role = "PENDING";
        String phoneNumber = "380000000000";
        String address = "Neverland 11";

        UserInfo userInfo = new UserInfo(
            UUID.randomUUID().toString(),
            email,
            firstName,
            lastName,
            phoneNumber,
            role,
            address
        );

        RequestTypeDTO requestTypeStaff = new RequestTypeDTO(
            UUID.randomUUID().toString(),
            RequestTypeEnum.BECOME_STAFF
        );
        RequestTypeDTO requestTypeStudent = new RequestTypeDTO(
            UUID.randomUUID().toString(),
            RequestTypeEnum.BECOME_STUDENT
        );
        RequestTypeDTO requestTypeTeacher = new RequestTypeDTO(
            UUID.randomUUID().toString(),
            RequestTypeEnum.BECOME_TEACHER
        );

        RequestStatusDTO requestStatusDTO = new RequestStatusDTO(
            UUID.randomUUID().toString(),
            RequestStatusEnum.PENDING
        );

        UserRequestDTO userRequestDTO = new UserRequestDTO(
            UUID.randomUUID().toString(),
            "123",
            null,
            null,
            requestStatusDTO
        );

        when(pendingUserService.findById(any(UUID.class)))
            .thenReturn(new PendingUserProfile(
                UUID.randomUUID().toString(),
                null,
                null,
                null,
                null,
                null
            ));

        doNothing()
            .when(studentService)
            .createStudentFromPendingUser(any(PendingUserProfile.class));
        doNothing()
            .when(teacherService)
            .createTeacherFromPendingUser(any(PendingUserProfile.class));
        doNothing()
            .when(staffService)
            .createStaffFromPendingUser(any(PendingUserProfile.class));

        doNothing()
            .when(userRequestService)
            .approveRequest(any(UUID.class));

        userRequestDTO.setRequestTypeDTO(requestTypeStudent);
        adminService.grantRoleOnRequest(userRequestDTO);
        verify(studentService)
            .createStudentFromPendingUser(any(PendingUserProfile.class));

        userRequestDTO.setRequestTypeDTO(requestTypeTeacher);
        adminService.grantRoleOnRequest(userRequestDTO);
        verify(teacherService)
            .createTeacherFromPendingUser(any(PendingUserProfile.class));

        userRequestDTO.setRequestTypeDTO(requestTypeStaff);
        adminService.grantRoleOnRequest(userRequestDTO);
        verify(staffService)
            .createStaffFromPendingUser(any(PendingUserProfile.class));

        verify(userRequestService, times(3))
            .approveRequest(any(UUID.class));
    }
}
