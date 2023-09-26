package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.repository.AdminRepository;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.service.mapper.AdminProfileMapper;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.RequestValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final AdminProfileMapper adminProfileMapper;

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
        Admin admin = adminRepository.findByEmail(passwordChangeRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        passwordValidator.validatePasswordChangeRequest(passwordChangeRequest);

        admin.setPassword(passwordChangeRequest.getNewPassword());

        adminRepository.save(admin);
    }

    @Transactional
    public AdminProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Admin admin = adminRepository.findByEmail(email).orElseThrow(BadCredentialsException::new);

        return adminProfileMapper.mapAdminEntityToAdminProfile(admin);
    }

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        adminRepository.save(
            new Admin(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword())
            )
        );
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

    private UUID getAdminIdByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }
}
