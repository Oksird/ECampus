package ua.foxminded.muzychenko.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.WeekNumber;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private Map<WeekNumber, Map<DayOfWeek, List<LessonInfo>>> twoWeeksSchedule;
}
