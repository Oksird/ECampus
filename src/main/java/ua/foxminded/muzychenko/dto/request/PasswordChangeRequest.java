package ua.foxminded.muzychenko.dto.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PasswordChangeRequest {
    private final String email;
    private final String encodedRealPassword;
    private final String oldPassword;
    private final String newPassword;
    private final String repeatedNewPassword;
}
