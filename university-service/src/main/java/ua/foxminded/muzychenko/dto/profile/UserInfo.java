package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserInfo {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private String address;
}
