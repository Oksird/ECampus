package ua.foxminded.muzychenko.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationRequest {
    private final String email;
    private final String password;
    private final String repeatPassword;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String address;
}
