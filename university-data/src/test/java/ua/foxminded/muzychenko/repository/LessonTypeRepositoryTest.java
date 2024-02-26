package ua.foxminded.muzychenko.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.LessonType;
import ua.foxminded.muzychenko.enums.TypeOfLesson;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = LessonTypeRepository.class)
@Import(DataTestConfig.class)
@Transactional
class LessonTypeRepositoryTest {

    @Autowired
    private LessonTypeRepository lessonTypeRepository;

    @Test
    void findLessonTypeByTypeShouldReturnLessonTypeEntity() {
        LessonType expectedType = lessonTypeRepository.findAll()
            .stream()
            .filter(lessonType -> lessonType.getType().equals(TypeOfLesson.LECTURE))
            .findFirst()
            .orElse(null);

        LessonType actualType = lessonTypeRepository.findLessonTypeByType(TypeOfLesson.LECTURE)
            .orElseThrow(EntityNotFoundException::new);

        assertEquals(expectedType, actualType);
    }
}