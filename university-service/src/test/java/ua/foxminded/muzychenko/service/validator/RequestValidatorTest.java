package ua.foxminded.muzychenko.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;
import ua.foxminded.muzychenko.service.validator.exception.InvalidFieldException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(classes = RequestValidator.class)
class RequestValidatorTest {

    @MockBean
    private PasswordValidator passwordValidator;
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
    void validateUserLoginRequestShouldNotThrowAnyException() {
        String encodedPass = "d(ASNU";

        UserLoginRequest request = new UserLoginRequest(
            "email@gmail.com",
            "password123!W"
        );

        doNothing().when(passwordValidator).validateEnteredPassword(any(String.class), eq(request.getPassword()));

        assertDoesNotThrow(() -> requestValidator.validateUserLoginRequest(request, encodedPass, request.getEmail()));
    }

    @Test
    void validateUserLoginRequestShouldThrowBadCredentialsExceptionWhenEmailIsIncorrect() {
        String encodedPass = "d(ASNU";
        String realEmail = "real@email.com";

        UserLoginRequest request = new UserLoginRequest(
            "email@gmail.com",
            "password123!W"
        );

        doNothing().when(passwordValidator).validateEnteredPassword(any(String.class), eq(request.getPassword()));

        assertThrows(BadCredentialsException.class,
            () -> requestValidator.validateUserLoginRequest(request, encodedPass, realEmail));
    }

    @Test
    void validateUserLoginRequestShouldThrowBadCredentialsExceptionWhenPasswordIsWrong() {
        String encodedPass = "d(ASNU";
        String realEmail = "real@email.com";

        UserLoginRequest request = new UserLoginRequest(
            "email@gmail.com",
            "password123!W"
        );

        doThrow(BadCredentialsException.class)
            .when(passwordValidator).validateEnteredPassword(encodedPass, request.getPassword());

        assertThrows(BadCredentialsException.class,
            () -> requestValidator.validateUserLoginRequest(request, encodedPass, realEmail));
    }

    @Test
    void validateUserRegistrationRequestShouldThrowNPEWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> requestValidator.validateUserRegistrationRequest(null));
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