package ua.foxminded.muzychenko.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.StaffProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.Staff;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.StaffRepository;
import ua.foxminded.muzychenko.service.mapper.StaffProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final PendingUserRepository pendingUserRepository;
    private final StaffRepository staffRepository;
    private final StaffProfileMapper mapper;
    private final RequestValidator requestValidator;

    @Transactional
    public void createStaffFromPendingUser(PendingUserProfile pendingUserProfile) {

        PendingUser pendingUser = pendingUserRepository.findById(UUID.fromString(pendingUserProfile.getUserId()))
            .orElseThrow(UserNotFoundException::new);

        pendingUserRepository.delete(pendingUser);

        Staff staff = new Staff(
            UUID.randomUUID(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPassword()
        );

        staffRepository.save(staff);
    }

    @Transactional(readOnly = true)
    public Page<StaffProfile> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Staff> studentPage = staffRepository.findAll(pageable);
        return studentPage.map(mapper::mapStaffEntityToProfile);
    }

    @Transactional(readOnly = true)
    public StaffProfile findStaffById(UUID id) {
        return mapper.mapStaffEntityToProfile(
            staffRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new)
        );
    }

    @Transactional(readOnly = true)
    public StaffProfile findStaffByEmail(String email) {
        return mapper.mapStaffEntityToProfile(
            staffRepository
                .findByEmail(email)
                .orElseThrow(UserNotFoundException::new)
        );
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        requestValidator.validatePasswordChangeRequest(passwordChangeRequest);

        Staff staff = staffRepository
            .findByEmail(passwordChangeRequest.getEmail())
            .orElseThrow(UserNotFoundException::new);

        staff.setPassword(passwordChangeRequest.getNewPassword());

        staffRepository.save(staff);
    }

    @Transactional
    public void deleteStaff(UUID id) {
        staffRepository.deleteById(id);
    }
}
