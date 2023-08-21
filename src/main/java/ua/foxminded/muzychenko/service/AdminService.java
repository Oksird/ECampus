package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.RequestValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminDao adminDao;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public Admin findAdminById(UUID id) {
        return adminDao.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<Admin> findAllAdmins(Long pageNumber, Long pageSize) {
        return adminDao.findAll(pageNumber, pageSize);
    }

    public void deleteAdmin(String email) {
        adminDao.deleteById(getAdminIdByEmail(email));
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        Admin admin = adminDao.findByEmail(passwordChangeRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        passwordValidator.validatePasswordChangeRequest(passwordChangeRequest);

        admin.setPassword(passwordChangeRequest.getNewPassword());

        adminDao.update(admin.getUserId(), admin);
    }

    @Transactional
    public AdminProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Admin admin = adminDao.findByEmail(email).orElseThrow(BadCredentialsException::new);

        return new AdminProfile(
            admin.getFirstName(),
            admin.getLastName(),
            admin.getEmail()
        );
    }

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        adminDao.create(
            new Admin(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword())
            )
        );
    }

    private UUID getAdminIdByEmail(String email) {
        return adminDao.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }
}
