package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.enums.LessonNumber;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonTimeRepository extends JpaRepository<LessonTime, UUID> {

    Optional<LessonTime> findLessonTimeByLessonNumber(LessonNumber lessonNumber);

}
