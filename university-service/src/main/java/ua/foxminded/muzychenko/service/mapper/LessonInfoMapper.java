package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.entity.Lesson;

@Mapper
@AllArgsConstructor
public class LessonInfoMapper {

    private CourseInfoMapper courseInfoMapper;
    private GroupInfoMapper groupInfoMapper;

    public LessonInfo mapEntityToDTO(Lesson lesson) {

        return new LessonInfo(
            lesson.getId().toString(),
            lesson.getLessonType().getType(),
            courseInfoMapper.mapCourseEntityToCourseInfo(lesson.getCourse()),
            groupInfoMapper.mapGroupEntityToGroupInfo(lesson.getGroup()),
            lesson.getStudyDay().getDayOfWeek(),
            lesson.getStudyWeek().getWeekNumber(),
            lesson.getLessonTime().getLessonNumber(),
            lesson.getAdditionalInfo()
        );
    }
}
