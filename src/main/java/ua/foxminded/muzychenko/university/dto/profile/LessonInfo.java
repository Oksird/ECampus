package ua.foxminded.muzychenko.university.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
public class LessonInfo {
    private String lessonId;
    private String courseName;
    private TeacherProfile teacherProfile;
    private String groupName;
    private Date date;
    private Time startTime;
    private Time endTime;
}
