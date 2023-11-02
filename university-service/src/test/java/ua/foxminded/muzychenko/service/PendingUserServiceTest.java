package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.service.mapper.PendingUserMapper;
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
    classes = PendingUserService.class)
class PendingUserServiceTest {
    @MockBean
    private PendingUserRepository pendingUserRepository;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PendingUserMapper pendingUserMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PendingUserService pendingUserService;

    @Test
    void registerShouldCreateNewPendingUserFromUserRegRequest() {
        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "encryptedPass"
        );

        doNothing().when(requestValidator).validateUserRegistrationRequest(any(UserRegistrationRequest.class));

        when(passwordEncoder.encode(any(String.class))).thenReturn("encryptedPass");

        when(pendingUserRepository.save(any(PendingUser.class)))
            .thenReturn(pendingUser);

        pendingUserService.register(new UserRegistrationRequest(
            "em",
            "pass",
            "pass",
            "fn",
            "ln"
        ));

        verify(requestValidator).validateUserRegistrationRequest(any(UserRegistrationRequest.class));
        assertEquals(passwordEncoder.encode("qwe"), pendingUser.getPassword());
        assertEquals(pendingUserRepository.save(pendingUser), pendingUser);
    }

    @Test
    void findAdminByIdShouldReturnAdminWithCorrectID() {
        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        PendingUserProfile pendingUserProfile = new PendingUserProfile(
            pendingUser.getUserId().toString(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail()
        );

        when(pendingUserRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(pendingUser));

        when(pendingUserMapper.mapPendingUserEntityToProfile(any(PendingUser.class)))
            .thenReturn(pendingUserProfile);

        assertEquals(pendingUserProfile, pendingUserService.findById(UUID.randomUUID()));
    }

    @Test
    void findAllPUserShouldReturnListOfAllPUsersByPage() {
        List<PendingUser> pendingUsers = new ArrayList<>(List.of(
            new PendingUser(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass"
            ),
            new PendingUser(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2"
            )
        ));

        PendingUser pendingUser1 = pendingUsers.get(0);
        PendingUser pendingUser2 = pendingUsers.get(1);

        PendingUserProfile pendingUserProfile1 = new PendingUserProfile(pendingUser1.getUserId().toString() ,pendingUser1.getFirstName(), pendingUser1.getLastName(), pendingUser1.getEmail());
        PendingUserProfile pendingUserProfile2 = new PendingUserProfile(pendingUser2.getUserId().toString() ,pendingUser2.getFirstName(), pendingUser2.getLastName(), pendingUser2.getEmail());

        List<PendingUserProfile> pendingUserProfiles = new ArrayList<>(List.of(pendingUserProfile1, pendingUserProfile2));

        when(pendingUserMapper.mapPendingUserEntityToProfile(pendingUser1))
            .thenReturn(pendingUserProfile1);
        when(pendingUserMapper.mapPendingUserEntityToProfile(pendingUser2))
            .thenReturn(pendingUserProfile2);

        Page<PendingUser> expectedPage = new PageImpl<>(pendingUsers);

        when(pendingUserRepository.findAll(any(Pageable.class)))
            .thenReturn(expectedPage);

        assertEquals(pendingUserProfiles, pendingUserService.findAll(1,1).getContent());
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfPUserInDataBase() {
        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(pendingUserRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(pendingUser));

        doNothing()
            .when(requestValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        when(pendingUserRepository.save(any(PendingUser.class)))
            .thenReturn(pendingUser);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        pendingUserService.changePassword(passwordChangeRequest);

        verify(pendingUserRepository).findByEmail(any(String.class));
        verify(requestValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(pendingUserRepository).save(any(PendingUser.class));
    }

    @Test
    void deleteStaffShouldRemoveStaffFromDataBase() {
        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        doNothing()
            .when(pendingUserRepository)
            .deleteById(any(UUID.class));

        pendingUserService.deleteById(pendingUser.getUserId());

        verify(pendingUserRepository).deleteById(pendingUser.getUserId());
    }

    @Test
    void findPUserByEmailShouldReturnCorrectPUserProfile() {
        PendingUser pendingUser = new PendingUser(UUID.randomUUID(), "fn", "ln", "em", "pas");
        PendingUserProfile pendingUserProfile = new PendingUserProfile(pendingUser.getUserId().toString() ,pendingUser.getFirstName(), pendingUser.getLastName(), pendingUser.getEmail());
        when(pendingUserRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(pendingUser));
        when(pendingUserMapper.mapPendingUserEntityToProfile(pendingUser))
            .thenReturn(pendingUserProfile);

        assertEquals(pendingUserProfile, pendingUserService.findByEmail("email"));
    }
}
