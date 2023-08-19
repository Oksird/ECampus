package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.entity.UserType;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.RequestValidator;
import ua.foxminded.muzychenko.service.validator.exception.BadCredentialsException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherDao teacherDao;
    private final CourseDao courseDao;
    private final RequestValidator requestValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public Teacher findTeacherById(UUID id) {
        return teacherDao.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<Teacher> findAllTeachers(Long pageNumber, Long pageSize) {
        return teacherDao.findAll(pageNumber, pageSize);
    }

    public List<Teacher> findTeachersByCourse(String nameOfCourse) {
        return teacherDao.findByCourse(nameOfCourse);
    }

    public void deleteTeacher(String email) {
        teacherDao.deleteById(getTeacherIdByEmail(email));
    }

    public void excludeTeacherFromCourse(String email, String nameOfCourse) {
        teacherDao.excludeFromCourse(getTeacherIdByEmail(email), nameOfCourse);
    }

    public void addTeacherToCourse(String email, String nameOfCourse) {
        teacherDao.addToCourse(getTeacherIdByEmail(email), nameOfCourse);
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        Teacher teacher = teacherDao.findByEmail(passwordChangeRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        passwordValidator.validatePasswordChangeRequest(passwordChangeRequest);

        teacher.setPassword(passwordChangeRequest.getNewPassword());

        teacherDao.update(teacher.getUserId(), teacher);
    }

    @Transactional
    public TeacherProfile login(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();

        Teacher teacher = teacherDao.findByEmail(email).orElseThrow(BadCredentialsException::new);

        requestValidator.validateUserLoginRequest(userLoginRequest, teacher.getPassword(), email);

        List<CourseInfo> courseInfoList = new ArrayList<>();

        List<Course> courses = courseDao.findCoursesByUserIdAndUserType(teacher.getUserId(), UserType.TEACHER);

        courses.forEach(course -> courseInfoList.add(new CourseInfo(course.getCourseName(), course.getCourseDescription())));

        return new TeacherProfile(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseInfoList
        );
    }

    @Transactional
    public void register(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        teacherDao.create(
            new Teacher(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                passwordEncoder.encode(userRegistrationRequest.getPassword())
            )
        );
    }

    private UUID getTeacherIdByEmail(String email) {
        return teacherDao.findByEmail(email).orElseThrow(UserNotFoundException::new).getUserId();
    }
}
