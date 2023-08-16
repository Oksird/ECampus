package ua.foxminded.muzychenko.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Teacher extends User {
    private static final String USER_TYPE = "Teacher";

    public Teacher(@NonNull UUID teacherId, String firstName, String lastName, String email, String password) {
        super(teacherId, USER_TYPE, firstName, lastName, email, password);
    }
}
