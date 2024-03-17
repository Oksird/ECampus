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
    void validateUserRegistrationRequestShouldThrowExceptionWhenRequestIsWrong(UserRegistrationRequest userRegistrationRequest) {
        assertThrows(InvalidFieldException.class, () -> requestValidator.validateUserRegistrationRequest(userRegistrationRequest));
        System.out.println(userRegistrationRequest);
    }

    @Test
    void validateUserRegistrationRequestShouldThrowExceptionWhenRequestContainsDifferentPasswords() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email@user.com",
            "password!QW222",
            "WRONGdsd123!!@",
            "Maxim",
            "Petro",
            "380674882233",
            "Yakuba 33"
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
            "Petro",
            "380663281199",
            "Tasmowa 11"
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

        when(passwordEncoder.matches(any(), any(String.class)))
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

    @Test
    void validateUserRegistrationRequestShouldThrowInvalidFieldExcWhenPhoneNumberIsInWrongFormat() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email@mail.com",
            "password!W123",
            "password!W123",
            "Maxim",
            "Muzychenko",
            "    fsdbh   fhsd  ",
            "Tasmowa 3"
        );

        assertThrows(InvalidFieldException.class, () -> requestValidator.validateUserRegistrationRequest(userRegistrationRequest));
    }

    @Test
    void validateUserRegistrationRequestShouldThrowInvalidFieldExcWhenAddressIsInWrongFormat() {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email@mail.com",
            "password!W123",
            "password!W123",
            "Maxim",
            "Muzychenko",
            "380993332211",
            " 2 2   2   2   !@#@##"
        );

        assertThrows(InvalidFieldException.class, () -> requestValidator.validateUserRegistrationRequest(userRegistrationRequest));
    }

    @Test
    void validatePasswordChangeRequestShouldThrowBCExcWhenOldPassIsIncorrect() {
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class)))
            .thenReturn(false);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "email@mail.com",
            "qwe",
            "Qwerty123W",
            "Ll123456",
            "Ll123456"
        );

        assertThrows(BadCredentialsException.class, () -> requestValidator.validatePasswordChangeRequest(passwordChangeRequest));
    }

    @Test
    void validatePasswordChangeRequestShouldNotThrowAnyExceptionWhenEverythingIsCorrect() {
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class)))
            .thenReturn(true);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "email@mail.com",
            "qwe",
            "Qwerty123W",
            "Ll123456",
            "Ll123456"
        );

        assertDoesNotThrow(() -> requestValidator.validatePasswordChangeRequest(passwordChangeRequest));
    }

    static Stream<UserRegistrationRequest> userRegistrationRequests() {
        return Stream.of(
            new UserRegistrationRequest("e12d", "password2W!", "password2W!", "Max", "Joch","380283918822", "Tasmowa 11"),
            new UserRegistrationRequest("user@gmail.com", "password2W!", "password2W!", "123", "Joch","380283918822", "Tasmowa 11"),
            new UserRegistrationRequest("hser@mail.com", "qwe", "qwe", "Max", "User","380283918822", "Tasmowa 11"),
            new UserRegistrationRequest("hser@mail.com", "qwe", "qwe", "Max", "User","1", "Tasmowa 11"),
            new UserRegistrationRequest("hser@mail.com", "qwe", "qwe", "Max", "User","380283918822", "@  ?;4 ??%*   "),
            new UserRegistrationRequest("hser@mail.com", "password2W!A3@", "password2W!A3@", "Max", "123ds","380283918822", "Tasmowa 11")
        );
    }
}