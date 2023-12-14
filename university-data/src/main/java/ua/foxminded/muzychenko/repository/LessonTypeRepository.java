package ua.foxminded.muzychenko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.LessonType;
import ua.foxminded.muzychenko.enums.TypeOfLesson;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonTypeRepository extends JpaRepository<LessonType, UUID> {

    Optional<LessonType> findLessonTypeByType(TypeOfLesson type);

}
