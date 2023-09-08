package ua.foxminded.muzychenko.university.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.dao.CourseRepository;
import ua.foxminded.muzychenko.university.dao.GroupRepository;
import ua.foxminded.muzychenko.university.dao.LessonRepository;
import ua.foxminded.muzychenko.university.dao.StudentRepository;
import ua.foxminded.muzychenko.university.dao.TeacherRepository;
import ua.foxminded.muzychenko.university.dao.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.university.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.university.dao.exception.LessonNotFoundException;
import ua.foxminded.muzychenko.university.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.university.entity.Course;
import ua.foxminded.muzychenko.university.entity.Group;
import ua.foxminded.muzychenko.university.entity.Lesson;
import ua.foxminded.muzychenko.university.entity.Student;
import ua.foxminded.muzychenko.university.entity.Teacher;
import ua.foxminded.muzychenko.university.service.mapper.LessonInfoMapper;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonInfoMapper lessonInfoMapper;

    @Transactional
    public void createLesson(LessonInfo lessonInfo) {
        Lesson lesson = new Lesson(
            UUID.randomUUID(),
            courseRepository.findByCourseName(lessonInfo.getCourseName()).orElseThrow(CourseNotFoundException::new),
            groupRepository.findByGroupName(lessonInfo.getGroupName()).orElseThrow(GroupNotFoundException::new),
            teacherRepository.findByEmail(lessonInfo.getTeacherProfile().getEmail()).orElseThrow(UserNotFoundException::new),
            lessonInfo.getDate(),
            lessonInfo.getStartTime(),
            lessonInfo.getStartTime()
            );
        lessonRepository.save(lesson);
    }

    @Transactional(readOnly = true)
    public LessonInfo findLessonById(UUID lessonId) {
        return lessonInfoMapper.mapLessonEntityToLessonInfo(getLessonById(lessonId));
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findAllLessons() {
        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findAll()
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );

        return lessonInfos;
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findAllLessons(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findAll(pageable)
            .getContent()
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );

        return lessonInfos;
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findLessonByDate(Date date) {
        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findByDate(date)
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );
        return lessonInfos;
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findTeacherLessons(String teacherEmail) {
        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findByTeacherEmail(teacherEmail)
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );
        return lessonInfos;
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findLessonByGroupName(String groupName) {
        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findByGroupGroupName(groupName)
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );
        return lessonInfos;
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findLessonByCourseName(String courseName) {
        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findByCourseCourseName(courseName)
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );
        return lessonInfos;
    }

    @Transactional(readOnly = true)
    public List<LessonInfo> findStudentLessons(String studentEmail) {
        Student student = studentRepository
            .findByEmail(studentEmail)
            .orElseThrow(UserNotFoundException::new);

        List<LessonInfo> lessonInfos = new ArrayList<>();
        lessonRepository
            .findByGroupGroupName(student.getGroup().getGroupName())
            .forEach(
                lesson -> lessonInfos.add(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            );
        return lessonInfos;
    }

    @Transactional
    public void changeLessonDate(UUID lessonId, Date newDate) {
        Lesson lesson = getLessonById(lessonId);

        lesson.setDate(newDate);

        lessonRepository.save(lesson);
    }

    @Transactional
    public void changeLessonTime(UUID lessonId, Time newStartTime, Time newEndTime) {
        Lesson lesson = getLessonById(lessonId);

        lesson.setStartTime(newStartTime);
        lesson.setEndTime(newEndTime);

        lessonRepository.save(lesson);
    }

    @Transactional
    public void changeLessonCourse(UUID lessonId, Course newCourse) {
        Lesson lesson = getLessonById(lessonId);

        lesson.setCourse(newCourse);

        lessonRepository.save(lesson);
    }

    @Transactional
    public void changeLessonGroup(UUID lessonId, String groupName) {
        Lesson lesson = getLessonById(lessonId);

        Group group = groupRepository.findByGroupName(groupName).orElseThrow(GroupNotFoundException::new);

        lesson.setGroup(group);

        lessonRepository.save(lesson);
    }

    @Transactional
    public void changeLessonTeacher(UUID lessonId, String teacherEmail) {
        Lesson lesson = getLessonById(lessonId);

        Teacher teacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(UserNotFoundException::new);

        lesson.setTeacher(teacher);

        lessonRepository.save(lesson);
    }

    @Transactional
    public void deleteLesson(UUID lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    private Lesson getLessonById(UUID id) {
        return lessonRepository
            .findById(id)
            .orElseThrow(LessonNotFoundException::new);
    }
}
