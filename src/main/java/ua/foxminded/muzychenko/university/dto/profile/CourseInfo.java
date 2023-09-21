package ua.foxminded.muzychenko.university.dto.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CourseInfo {
    private String courseId;
    private String courseName;
    private String courseDescription;
}
