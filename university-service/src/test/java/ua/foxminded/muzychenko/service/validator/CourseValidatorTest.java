package ua.foxminded.muzychenko.service.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.service.validator.exception.InvalidFieldException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CourseValidator.class)
class CourseValidatorTest {

    @Autowired
    private CourseValidator validator;

    @Test
    void validateCourseInfoShouldThrowExceptionIfCourseNameIsInvalid() {
        String invalidCourseName = "2d21";
        String validCourseDescription = "Course about smth";

        CourseInfo courseInfo = new CourseInfo(
            UUID.randomUUID().toString(),
            invalidCourseName,
            validCourseDescription
        );

        assertThrows(
            InvalidFieldException.class,
            () -> validator.validateCourseInfo(
                courseInfo
            )
        );
    }

    @Test
    void validateCourseInfoShouldThrowExceptionIfCourseDescriptionIsInvalid() {
        String validCourseName = "Course";
        String emptyCourseDescription = "";

        CourseInfo courseInfo = new CourseInfo(
            UUID.randomUUID().toString(),
            validCourseName,
            emptyCourseDescription
        );

        assertThrows(
            InvalidFieldException.class,
            () -> validator.validateCourseInfo(
                courseInfo
            )
        );
    }

    @Test
    void validateCourseInfoShouldDoNothingIfCourseNameAndCourseDescriptionAreValid() {
        String validCourseName = "Course";
        String validCourseDescription = "Course about course";

        CourseInfo courseInfo = new CourseInfo(
            UUID.randomUUID().toString(),
            validCourseName,
            validCourseDescription
        );

        assertDoesNotThrow(() -> validator.validateCourseInfo(courseInfo));
    }
}
