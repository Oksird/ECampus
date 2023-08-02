package ua.foxminded.muzychenko.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class User {
    @NonNull
    private UUID userId;
    @NonNull
    private String userType;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    protected User(@NonNull UUID userId, @NonNull String userType, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
