package ua.foxminded.muzychenko.service.validator;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.muzychenko.config.Validator;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;
import ua.foxminded.muzychenko.service.validator.exception.InvalidFieldException;

import java.util.Objects;
import java.util.regex.Pattern;

@Validator
@AllArgsConstructor
public class RequestValidator {

    private PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.{8,})[^ ]{8,}$");
    private static final Pattern NAME_PATTERN =
        Pattern.compile("^[a-zA-Z- ]{2,25}$");

    public void validateUserRegistrationRequest(@NonNull UserRegistrationRequest userRegistrationRequest) {

        if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getRepeatPassword())) {
            throw new BadCredentialsException();
        }
        if (!isEmailValid(userRegistrationRequest.getEmail())) {
            throw new InvalidFieldException("Email");
        }
        if (!isPasswordValid(userRegistrationRequest.getPassword())) {
            throw new InvalidFieldException("Password");
        }
        if (!isFirstNameValid(userRegistrationRequest.getFirstName())) {
            throw new InvalidFieldException("First name");
        }
        if (!isLastNameValid(userRegistrationRequest.getLastName())) {
            throw new InvalidFieldException("Last name");
        }
    }

    public void validatePasswordChangeRequest(PasswordChangeRequest passwordChangeRequest) {
        if (!isPasswordValid(passwordChangeRequest.getNewPassword())) {
            throw new InvalidFieldException("Impossible new password");
        }

        if (!Objects.equals(passwordChangeRequest.getNewPassword(), passwordChangeRequest.getRepeatedNewPassword())) {
            throw new BadCredentialsException();
        }

        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), passwordChangeRequest.getEncodedRealPassword())) {
            throw new BadCredentialsException();
        }
    }

    private boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean isFirstNameValid(String firstName) {
        return NAME_PATTERN.matcher(firstName).matches();
    }

    private boolean isLastNameValid(String lastName) {
        return NAME_PATTERN.matcher(lastName).matches();
    }
}
