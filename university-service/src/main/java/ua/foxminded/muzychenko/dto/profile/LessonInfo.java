package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.LessonNumber;
import ua.foxminded.muzychenko.enums.TypeOfLesson;
import ua.foxminded.muzychenko.enums.WeekNumber;

@Data
@AllArgsConstructor
public class LessonInfo {
    private String lessonId;
    private TypeOfLesson type;
    private CourseInfo courseInfo;
    private GroupInfo groupInfo;
    private DayOfWeek dayOfWeek;
    private WeekNumber weekNumber;
    private LessonNumber lessonNumber;
    private String additionalInfo;
}
