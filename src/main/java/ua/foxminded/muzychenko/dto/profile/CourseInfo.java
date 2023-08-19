package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CourseInfo {
    private String courseName;
    private String courseDescription;
}
