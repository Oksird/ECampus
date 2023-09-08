package ua.foxminded.muzychenko.university.service.validator;

import ua.foxminded.muzychenko.university.config.Validator;

import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.service.validator.exception.InvalidFieldException;

import java.util.regex.Pattern;

@Validator
public class CourseValidator {
    private static final Pattern COURSE_NAME_PATTERN =
        Pattern.compile("^[A-Z][a-zA-Z\\s]*$");
    private static final Pattern COURSE_DESCRIPTION_PATTERN =
        Pattern.compile("^.+$", Pattern.MULTILINE);

    public void validateCourseInfo(CourseInfo courseInfo) {
        if (!COURSE_NAME_PATTERN.matcher(courseInfo.getCourseName()).matches()) {
            throw new InvalidFieldException("Invalid course name");
        }
        if (!COURSE_DESCRIPTION_PATTERN.matcher(courseInfo.getCourseDescription()).matches()) {
            throw new InvalidFieldException("Invalid course description");
        }
    }
}
