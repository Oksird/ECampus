package ua.foxminded.muzychenko.university.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.dao.AdminRepository;
import ua.foxminded.muzychenko.university.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.university.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.university.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.university.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.university.entity.Admin;
import ua.foxminded.muzychenko.university.service.mapper.AdminProfileMapper;
import ua.foxminded.muzychenko.university.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.university.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.university.service.validator.RequestValidator;
import ua.foxminded.muzychenko.university.service.validator.exception.BadCredentialsException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final AdminProfileMapper adminProfileMapper;

    public AdminProfile findAdminById(UUID id) {
        Admin admin = adminRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return adminProfileMapper.mapAdminEntityToAdminProfile(admin);
    }

    public List<AdminProfile> findAllAdmins(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Admin> adminList = adminRepository.findAll(pageable).getContent();
        List<AdminProfile> adminProfileList = new ArrayList<>(adminList.size());
        adminList.forEach(admin -> adminProfileList.add(adminProfileMapper.mapAdminEntityToAdminProfile(admin)));
        return adminProfileList;
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

    public AdminProfile findAdminByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return adminProfileMapper.mapAdminEntityToAdminProfile(admin);
    }

    private UUID getAdminIdByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }
}
