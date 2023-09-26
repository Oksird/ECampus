package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.entity.Lesson;

@Mapper
@AllArgsConstructor
public class LessonInfoMapper {
    private final TeacherProfileMapper teacherProfileMapper;

    public LessonInfo mapLessonEntityToLessonInfo(Lesson lesson) {

        return new LessonInfo(
            lesson.getLessonId().toString(),
            lesson.getCourse().getCourseName(),
            teacherProfileMapper.mapTeacherEntityToProfile(lesson.getTeacher(), lesson.getTeacher().getCourses()),
            lesson.getGroup().getGroupName(),
            lesson.getDate(),
            lesson.getStartTime(),
            lesson.getEndTime());
    }
}
