package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Lesson;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.entity.StudyWeek;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findLessonsByGroup(Group group);

    List<Lesson> findLessonsByCourse(Course course);

    Optional<Lesson> findByStudyWeekAndStudyDayAndLessonTime(StudyWeek studyWeek, StudyDay studyDay,
                                                             LessonTime lessonTime);

    List<Lesson> findLessonsByStudyWeekAndStudyDay(StudyWeek studyWeek, StudyDay studyDay);

    List<Lesson> findLessonsByStudyWeek(StudyWeek studyWeek);

}
