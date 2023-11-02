package ua.foxminded.muzychenko.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.service.mapper.PendingUserMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PendingUserService {

    private final PasswordEncoder passwordEncoder;
    private final PendingUserRepository repository;
    private final RequestValidator requestValidator;
    private final PendingUserMapper mapper;

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        repository.save(
            new PendingUser(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword())
            )
        );
    }

    @Transactional(readOnly = true)
    public Page<PendingUserProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<PendingUser> studentPage = repository.findAll(pageable);
        return studentPage.map(mapper::mapPendingUserEntityToProfile);
    }

    @Transactional(readOnly = true)
    public PendingUserProfile findById(UUID id) {
        return mapper.mapPendingUserEntityToProfile(repository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Transactional
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        requestValidator.validatePasswordChangeRequest(passwordChangeRequest);

        PendingUser pendingUser = repository
            .findByEmail(passwordChangeRequest.getEmail())
            .orElseThrow(UserNotFoundException::new);

        pendingUser.setPassword(passwordChangeRequest.getNewPassword());

        repository.save(pendingUser);
    }

    @Transactional(readOnly = true)
    public PendingUserProfile findByEmail(String email) {
        return mapper.mapPendingUserEntityToProfile(
            repository
                .findByEmail(email)
                .orElseThrow(UserNotFoundException::new)
        );
    }
}
