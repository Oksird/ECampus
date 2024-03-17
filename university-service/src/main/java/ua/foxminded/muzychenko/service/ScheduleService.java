package ua.foxminded.muzychenko.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.profile.LessonCreationDto;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.dto.profile.Schedule;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Lesson;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.entity.StudyWeek;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.WeekNumber;
import ua.foxminded.muzychenko.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.exception.LessonNotFoundException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.GroupRepository;
import ua.foxminded.muzychenko.repository.LessonRepository;
import ua.foxminded.muzychenko.repository.LessonTimeRepository;
import ua.foxminded.muzychenko.repository.LessonTypeRepository;
import ua.foxminded.muzychenko.repository.StudyDayRepository;
import ua.foxminded.muzychenko.repository.StudyWeekRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.service.mapper.LessonInfoMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final StudyDayRepository studyDayRepository;
    private final StudyWeekRepository studyWeekRepository;
    private final LessonTypeRepository lessonTypeRepository;
    private final LessonTimeRepository lessonTimeRepository;
    private final TeacherRepository teacherRepository;
    private final LessonInfoMapper lessonInfoMapper;

    @Transactional
    public void createLesson(LessonCreationDto lessonCreationDto) {
        Lesson lesson = new Lesson(
            UUID.randomUUID(),
            lessonTypeRepository.findLessonTypeByType(lessonCreationDto.getType()).orElseThrow(EntityNotFoundException::new),
            courseRepository.findByCourseName(lessonCreationDto.getCourseName()).orElseThrow(CourseNotFoundException::new),
            groupRepository.findByGroupName(lessonCreationDto.getGroupName()).orElseThrow(GroupNotFoundException::new),
            studyDayRepository.findStudyDayByDayOfWeek(lessonCreationDto.getDayOfWeek()).orElseThrow(EntityNotFoundException::new),
            studyWeekRepository.findStudyWeekByWeekNumber(lessonCreationDto.getWeekNumber()).orElseThrow(EntityNotFoundException::new),
            lessonTimeRepository.findLessonTimeByLessonNumber(lessonCreationDto.getLessonNumber()).orElseThrow(LessonNotFoundException::new),
            lessonCreationDto.getAdditionalInfo()
        );

        lessonRepository.save(lesson);
    }

    @Transactional(readOnly = true)
    public Schedule getScheduleForGroup(String groupName) {

        List<Lesson> lessons = lessonRepository.findLessonsByGroup(groupRepository.findByGroupName(groupName).orElseThrow(GroupNotFoundException::new));

        return mapLessonsToSchedule(lessons);
    }

    @Transactional(readOnly = true)
    public Schedule getScheduleForTeacher(String teacherEmail) {

        Teacher teacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(UserNotFoundException::new);
        Course course = teacher.getCourse();

        List<Lesson> lessons = lessonRepository.findLessonsByCourse(course);

        return mapLessonsToSchedule(lessons);
    }

    @Transactional
    public void editLesson(LessonCreationDto lessonCreationDto) {

        StudyWeek studyWeek = studyWeekRepository.findStudyWeekByWeekNumber(lessonCreationDto.getWeekNumber())
            .orElseThrow(EntityNotFoundException::new);
        StudyDay studyDay = studyDayRepository.findStudyDayByDayOfWeek(lessonCreationDto.getDayOfWeek())
            .orElseThrow(EntityNotFoundException::new);
        LessonTime lessonTime = lessonTimeRepository.findLessonTimeByLessonNumber(lessonCreationDto.getLessonNumber())
            .orElseThrow(EntityNotFoundException::new);

        Lesson lesson = lessonRepository.findByStudyWeekAndStudyDayAndLessonTime(studyWeek, studyDay, lessonTime)
            .orElseThrow(EntityNotFoundException::new);

        lesson.setCourse(courseRepository.findByCourseName(lessonCreationDto.getCourseName())
            .orElseThrow(CourseNotFoundException::new));
        lesson.setGroup(groupRepository.findByGroupName(lessonCreationDto.getGroupName())
            .orElseThrow(GroupNotFoundException::new));
        lesson.setAdditionalInfo(lessonCreationDto.getAdditionalInfo());
        lesson.setLessonType(lessonTypeRepository.findLessonTypeByType(lessonCreationDto.getType())
            .orElseThrow(EntityNotFoundException::new));

        lessonRepository.save(lesson);
    }

    private Schedule mapLessonsToSchedule(List<Lesson> lessons) {
        Map<WeekNumber, Map<DayOfWeek, List<LessonInfo>>> twoWeeksSchedule =
            lessons.stream()
                .collect(Collectors.groupingBy(
                    lesson -> lesson.getStudyWeek().getWeekNumber(),
                    Collectors.groupingBy(
                        lesson -> lesson.getStudyDay().getDayOfWeek(),
                        Collectors.mapping(
                                lessonInfoMapper::mapEntityToDTO,
                            Collectors.toList()
                        )
                    )
                ));

        return new Schedule(twoWeeksSchedule);
    }

}
