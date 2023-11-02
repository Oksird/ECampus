package ua.foxminded.muzychenko.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;
import ua.foxminded.muzychenko.service.validator.exception.InvalidFieldException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RequestValidator.class)
class RequestValidatorTest {

    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RequestValidator requestValidator;

    @ParameterizedTest
    @MethodSource("userRegistrationRequests")
    void validateUserRegistrationRequestShouldThrowExceptionWhenRequestIsWrong
        (UserRegistrationRequest userRegistrationRequest) {
        assertThrows(InvalidFieldException.class, () -> requestValidator.validateUserRegistrationRequest(userRegistrationRequest));
    }

    @Test
    void validateUserRegistrationRequestShouldThrowExceptionWhenRequestContainsDifferentPasswords() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email@user.com",
            "password!QW222",
            "WRONGdsd123!!@",
            "Maxim",
            "Petro"
        );

        assertThrows(BadCredentialsException.class, () -> requestValidator.validateUserRegistrationRequest(userRegistrationRequest));
    }

    @Test
    void validateUserRegistrationRequestShouldNotThrowExceptionWhenRequestIsCorrect() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email@user.com",
            "password!QW222",
            "password!QW222",
            "Maxim",
            "Petro"
        );

        assertDoesNotThrow(() -> requestValidator.validateUserRegistrationRequest(userRegistrationRequest));
    }

    @Test
    void validateUserRegistrationRequestShouldThrowNPEWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> requestValidator.validateUserRegistrationRequest(null));
    }

    @Test
    void validatePasswordChangeRequestShouldThrowInvalidFieldExceptionWhenNewPasswordIsWrongFormat() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "email",
            "encrp",
            "old",
            "нвірпс",
            "нвірпс"
        );

        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(true);

        assertThrows(
            InvalidFieldException.class,
            () -> requestValidator.validatePasswordChangeRequest(passwordChangeRequest)
        );
    }

    @Test
    void validatePasswordChangeRequestShouldThrowBadCredExceptionWhenRepeatedPassNotEqualsOld() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "email",
            "encrp",
            "old",
            "dajs7asd!dSSJ",
            "passwordQWE123!"
        );

        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(true);

        assertThrows(
            BadCredentialsException.class,
            () -> requestValidator.validatePasswordChangeRequest(passwordChangeRequest)
        );
    }

    @Test
    void validatePasswordChangeRequestShouldThrowBadCredExcWhenOldPassIsIncorrect() {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "email",
            "encrp",
            "old",
            "dajs7asd!dSSJ",
            "passwordQWE123!"
        );

        when(passwordEncoder.matches(any(String.class), any(String.class)))
            .thenReturn(false);

        assertThrows(
            BadCredentialsException.class,
            () -> requestValidator.validatePasswordChangeRequest(passwordChangeRequest)
        );
    }

    static Stream<UserRegistrationRequest> userRegistrationRequests() {
        return Stream.of(
            new UserRegistrationRequest("e12d", "password2W!", "password2W!", "Max", "Joch"),
            new UserRegistrationRequest("user@gmail.com", "password2W!", "password2W!", "123", "Joch"),
            new UserRegistrationRequest("hser@mail.com", "qwe", "qwe", "Max", "User"),
            new UserRegistrationRequest("hser@mail.com", "password2W!A3@", "password2W!A3@", "Max", "123ds")
        );
    }
}