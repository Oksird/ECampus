package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CourseInfo {
    private String courseId;
    private String courseName;
    private String courseDescription;
    private TeacherProfile teacherProfile;
}
