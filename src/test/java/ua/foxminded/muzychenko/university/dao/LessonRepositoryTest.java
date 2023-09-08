package ua.foxminded.muzychenko.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.entity.Lesson;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestUniversityApplication.class)
@Transactional
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    void findByDateShouldReturnAllLessonsOnDate() {
        Date date = Date.valueOf("2023-09-06");

        List<Lesson> expectedLessons = lessonRepository
            .findAll()
            .stream()
            .filter(
                lesson -> lesson.getDate().equals(date))
            .toList();

        assertEquals(expectedLessons, lessonRepository.findByDate(date));
    }

    @Test
    void findByGroupNameShouldReturnAllLessonsOfGroup() {
        String groupName = "AA-01";

        List<Lesson> expectedLessons = lessonRepository
            .findAll()
            .stream()
            .filter(
                lesson -> lesson.getGroup()
                    .getGroupName()
                    .equals(groupName))
            .toList();

        assertEquals(expectedLessons, lessonRepository.findByGroupGroupName(groupName));
    }

    @Test
    void findByCourseNameShouldReturnAllLessonsOfCourse() {
        String courseName = "Course1";

        List<Lesson> expectedLessons = lessonRepository
            .findAll()
            .stream()
            .filter(
                lesson -> lesson.getCourse()
                    .getCourseName()
                    .equals(courseName))
            .toList();

        assertEquals(expectedLessons, lessonRepository.findByCourseCourseName(courseName));
    }

    @Test
    void findByTeacherEmailShouldReturnAllLessonOfTeacher() {
        String teacherEmail = "et2";

        List<Lesson> expectedLessons = lessonRepository
            .findAll()
            .stream()
            .filter(
                lesson -> lesson.getTeacher()
                    .getEmail()
                    .equals(teacherEmail))
            .toList();

        assertEquals(expectedLessons, lessonRepository.findByTeacherEmail(teacherEmail));
    }
}
