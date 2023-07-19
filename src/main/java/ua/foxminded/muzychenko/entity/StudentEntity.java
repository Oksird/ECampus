package ua.foxminded.muzychenko.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class StudentEntity {
    private long studentId;
    private long groupId;
    private String firstName;
    private String lastName;
}
