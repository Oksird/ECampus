package ua.foxminded.muzychenko.university.service.validator;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.university.config.Validator;
import ua.foxminded.muzychenko.university.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.university.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.university.service.validator.exception.BadCredentialsException;

@Validator
@AllArgsConstructor
public class PasswordValidator {

    private final PasswordEncoder passwordEncoder;

    public void validatePasswordChangeRequest(PasswordChangeRequest passwordChangeRequest) {
        String encodedRealPassword = passwordChangeRequest.getEncodedRealPassword();
        String oldPassword = passwordChangeRequest.getOldPassword();
        String newPassword = passwordChangeRequest.getNewPassword();
        String newRepeatedPassword = passwordChangeRequest.getRepeatedNewPassword();

        if (!passwordEncoder.matches(oldPassword, encodedRealPassword)) {
            throw new BadCredentialsException();
        }
        if (!newPassword.equals(newRepeatedPassword)) {
            throw new BadCredentialsException();
        }
    }

    public void validateEnteredPassword(String encodedRealPassword, String enteredPassword) {
        if (!passwordEncoder.matches(encodedRealPassword, enteredPassword)) {
            throw new BadCredentialsException();
        }
    }
}
