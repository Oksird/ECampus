package ua.foxminded.muzychenko.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class CourseEntity {
    private long courseId;
    private String courseName;
    private String courseDescription;
}
