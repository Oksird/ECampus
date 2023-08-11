package ua.foxminded.muzychenko.service.util;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import ua.foxminded.muzychenko.annotation.CustomValidator;
import ua.foxminded.muzychenko.dto.UserRegistrationRequest;
import ua.foxminded.muzychenko.exception.BadCredentialsException;
import ua.foxminded.muzychenko.exception.InvalidFieldException;
import ua.foxminded.muzychenko.exception.PasswordMismatchException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.Optional;
import java.util.regex.Pattern;

@CustomValidator
@AllArgsConstructor
public class Validator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.{8,})[^ ]{8,}$";
    private static final String NAME_PATTERN = "^[a-zA-Z- ]{2,25}$";

    private final PasswordEncoder passwordEncoder;

    private boolean isEmailValid(String email) {
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        return emailPattern.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        Pattern passwrodPattern = Pattern.compile(PASSWORD_PATTERN);
        return passwrodPattern.matcher(password).matches();
    }

    private boolean isFirstNameValid(String firstName) {
        Pattern firstNamePattern = Pattern.compile(NAME_PATTERN);
        return firstNamePattern.matcher(firstName).matches();
    }

    private boolean isLastNameValid(String lastName) {
        Pattern lastNamePattern = Pattern.compile(NAME_PATTERN);
        return lastNamePattern.matcher(lastName).matches();
    }

    public void validateUserRegistrationRequest(@NonNull UserRegistrationRequest userRegistrationRequest) {

        if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getRepeatPassword())) {
            throw new PasswordMismatchException();
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

    public void validatePasswordChange(String oldPassword, String newPassword,
                                       String repeatNewPassword, String realCurrentPassword) {
        if (!passwordEncoder.matches(oldPassword, realCurrentPassword)) {
            throw new BadCredentialsException("Wrong password");
        }
        if (!newPassword.equals(repeatNewPassword)) {
            throw new PasswordMismatchException();
        }
    }

    public void validateLoginRequest(Optional<?> optionalUser, String providedPassword, String encryptedPassword) {
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the provided email does not exist");
        }

        if (!passwordEncoder.matches(providedPassword, encryptedPassword)) {
            throw new BadCredentialsException("Invalid password");
        }
    }
}
