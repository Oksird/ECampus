package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.UserInfo;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.AdminRepository;
import ua.foxminded.muzychenko.service.mapper.AdminProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService {
    private final RequestValidator requestValidator;
    private final AdminRepository adminRepository;
    private final AdminProfileMapper adminProfileMapper;
    private final UserRequestService userRequestService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StaffService staffService;

    @Transactional(readOnly = true)
    public AdminProfile findAdminById(UUID id) {
        Admin admin = adminRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return adminProfileMapper.mapAdminEntityToAdminProfile(admin);
    }

    @Transactional
    public void deleteAdmin(String email) {
        adminRepository.deleteById(getAdminIdByEmail(email));
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        requestValidator.validatePasswordChangeRequest(passwordChangeRequest);

        Admin admin = adminRepository.findByEmail(passwordChangeRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        admin.setPassword(passwordChangeRequest.getNewPassword());

        adminRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public AdminProfile findAdminByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return adminProfileMapper.mapAdminEntityToAdminProfile(admin);
    }

    @Transactional(readOnly = true)
    public Page<AdminProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Admin> adminPage = adminRepository.findAll(pageable);
        return adminPage.map(adminProfileMapper::mapAdminEntityToAdminProfile);
    }

    @Transactional
    public void grantRoleOnRequest(UserRequestDTO userRequestDTO) {

        UserInfo userInfo = userRequestDTO.getUserInfo();

        PendingUserProfile pendingUserProfile = new PendingUserProfile(
            userInfo.getId(),
            userInfo.getFirstName(),
            userInfo.getLastName(),
            userInfo.getEmail(),
            userInfo.getPhoneNumber(),
            userInfo.getAddress()
        );

        switch (userRequestDTO.getRequestTypeDTO().getType()) {
            case BECOME_STUDENT -> studentService.createStudentFromPendingUser(pendingUserProfile);
            case BECOME_TEACHER -> teacherService.createTeacherFromPendingUser(pendingUserProfile);
            case BECOME_STAFF -> staffService.createStaffFromPendingUser(pendingUserProfile);
        }

        userRequestService.approveRequest(userRequestDTO);

    }

    private UUID getAdminIdByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }
}
