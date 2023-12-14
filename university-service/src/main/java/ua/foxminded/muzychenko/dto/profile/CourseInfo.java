package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class CourseInfo {
    private String courseId;
    private String courseName;
    private String courseDescription;
    private TeacherProfile teacherProfile;
}
