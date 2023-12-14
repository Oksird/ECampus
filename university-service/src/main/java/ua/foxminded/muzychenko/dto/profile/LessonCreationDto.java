package ua.foxminded.muzychenko.dto.profile;

import lombok.Data;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.LessonNumber;
import ua.foxminded.muzychenko.enums.TypeOfLesson;
import ua.foxminded.muzychenko.enums.WeekNumber;

@Data
public class LessonCreationDto {
    private WeekNumber weekNumber;
    private DayOfWeek dayOfWeek;
    private LessonNumber lessonNumber;
    private TypeOfLesson type;
    private String groupName;
    private String courseName;
    private String additionalInfo;
}
