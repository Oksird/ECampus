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
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.enums.LessonNumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = LessonTimeRepository.class)
@Import(DataTestConfig.class)
@Transactional
class LessonTimeRepositoryTest {

    @Autowired
    private LessonTimeRepository lessonTimeRepository;

    @Test
    void findLessonTimeByLessonNumberShouldReturnCorrectLessonTime() {
        LessonTime expectedLessonTime = lessonTimeRepository.findAll()
            .stream()
            .filter(lessonTime -> lessonTime.getLessonNumber().equals(LessonNumber.FIRST))
            .findFirst()
            .orElse(null);

        LessonTime actualLessonTime = lessonTimeRepository.findLessonTimeByLessonNumber(LessonNumber.FIRST)
            .orElseThrow(EntityNotFoundException::new);

        assertEquals(expectedLessonTime, actualLessonTime);
    }
}