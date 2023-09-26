package ua.foxminded.muzychenko.service.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PasswordValidator.class)
class PasswordValidatorTest {

    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordValidator validator;

    @Test
    void validatePasswordChangeRequestShouldThrowBadCredentialExceptionWhenPasswordIsIncorrect() {
        String email = "email@em.com";
        String encodedRealPassword = "encodedRealPass";
        String oldPassword = "realPass";
        String newPassword = "newPass";
        String newRepeatedPassword = "newPass";

        PasswordChangeRequest request = new PasswordChangeRequest(
            email,
            encodedRealPassword,
            oldPassword,
            newPassword,
            newRepeatedPassword
        );

        when(passwordEncoder.matches(oldPassword, encodedRealPassword))
            .thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> validator.validatePasswordChangeRequest(request));
    }

    @Test
    void validatePasswordChangeRequestShouldThrowBadCredentialExceptionWhenPNewPasswordWasNotBeenRepeated() {
        String email = "email@em.com";
        String encodedRealPassword = "encodedRealPass";
        String oldPassword = "realPass";
        String newPassword = "newPass";
        String newRepeatedPassword = "newPassWrong";

        PasswordChangeRequest request = new PasswordChangeRequest(
            email,
            encodedRealPassword,
            oldPassword,
            newPassword,
            newRepeatedPassword
        );

        when(passwordEncoder.matches(oldPassword, encodedRealPassword))
            .thenReturn(true);

        assertThrows(BadCredentialsException.class, () -> validator.validatePasswordChangeRequest(request));
    }

    @Test
    void validatePasswordChangeRequestShouldNotThrowAnyExceptionWhenPasswordChangeRequestIsCorrect() {
        String email = "email@em.com";
        String encodedRealPassword = "encodedRealPass";
        String oldPassword = "realPass";
        String newPassword = "newPass";
        String newRepeatedPassword = "newPass";

        PasswordChangeRequest request = new PasswordChangeRequest(
            email,
            encodedRealPassword,
            oldPassword,
            newPassword,
            newRepeatedPassword
        );

        when(passwordEncoder.matches(oldPassword, encodedRealPassword))
            .thenReturn(true);

        assertDoesNotThrow(() -> validator.validatePasswordChangeRequest(request));
    }

    @Test
    void validateEnteredPasswordShouldThrowBadCredentialsExceptionIfPasswordIsIncorrect() {
        String enteredPass = "abc";
        String encryptedPass = "f139n";

        when(passwordEncoder.matches(encryptedPass, enteredPass))
            .thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> validator.validateEnteredPassword(encryptedPass, enteredPass));
    }

    @Test
    void validateEnteredPasswordShouldNotThrowBadCredentialsExceptionIfPasswordIsCorrect() {
        String enteredPass = "abc";
        String encryptedPass = "f139n";

        when(passwordEncoder.matches(encryptedPass, enteredPass))
            .thenReturn(true);

        assertDoesNotThrow(() -> validator.validateEnteredPassword(encryptedPass, enteredPass));
    }
}