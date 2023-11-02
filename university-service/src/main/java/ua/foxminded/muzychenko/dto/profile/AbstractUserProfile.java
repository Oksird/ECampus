package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public abstract class AbstractUserProfile {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
