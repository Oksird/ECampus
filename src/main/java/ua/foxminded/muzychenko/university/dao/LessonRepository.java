package ua.foxminded.muzychenko.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.entity.Lesson;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @Transactional(readOnly = true)
    List<Lesson> findByDate(Date date);

    @Transactional(readOnly = true)
    List<Lesson> findByGroupGroupName(String groupName);

    @Transactional(readOnly = true)
    List<Lesson> findByCourseCourseName(String courseName);

    @Transactional(readOnly = true)
    List<Lesson> findByTeacherEmail(String email);
}
