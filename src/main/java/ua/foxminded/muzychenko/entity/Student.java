package ua.foxminded.muzychenko.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Student extends User {
    private static final String USER_TYPE = "Student";
    private UUID groupId;

    public Student(@NonNull UUID studentId , String firstName, String lastName, String email, String password,
                   UUID groupId) {
        super(studentId, USER_TYPE, firstName, lastName, email, password);
        this.groupId = groupId;
    }
}
