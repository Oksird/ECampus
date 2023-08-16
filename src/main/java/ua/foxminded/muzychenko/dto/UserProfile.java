package ua.foxminded.muzychenko.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class UserProfile {
    private String firstName;
    private String lastName;
    private String email;
}
