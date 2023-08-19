package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.TeacherDao;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class TeacherServiceTest {
    @MockBean
    private TeacherDao teacherDao;
    @MockBean
    private CourseDao courseDao;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PasswordValidator passwordValidator;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TeacherService teacherService;

    @Test
    void findTeacherByIdShouldReturnTeacherWithCorrectID() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        when(teacherDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(teacher));
        assertEquals(teacher, teacherService.findTeacherById(UUID.randomUUID()));
    }

    @Test
    void findAllTeachersShouldReturnListOfAllTeachersByPage() {
        List<Teacher> teacherList = new ArrayList<>(List.of(
            new Teacher(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass"
            ),
            new Teacher(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2"
            )
        ));

        when(teacherDao.findAll(any(Long.class), any(Long.class)))
            .thenReturn(teacherList);

        assertEquals(teacherList, teacherService.findAllTeachers(1L,1L));
    }

    @Test
    void findTeachersByCourseShouldReturnAllTeachersRelatedToCourse() {
        List<Teacher> teacherList = new ArrayList<>(List.of(
            new Teacher(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass"
            ),
            new Teacher(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2"
            )
        ));

        when(teacherDao.findByCourse(any(String.class)))
            .thenReturn(teacherList);

        assertEquals(teacherList, teacherService.findTeachersByCourse("cn"));
    }

    @Test
    void loginShouldReturnCorrectTeacherProfile() {

        Course course = new Course(UUID.randomUUID(), "cName", "cDesc");

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );
        doNothing()
            .when(teacherDao)
            .create(any(Teacher.class)
            );

        doNothing()
            .when(passwordValidator)
            .validateEnteredPassword(
                any(String.class),
                any(String.class)
            );

        doNothing()
            .when(requestValidator)
            .validateUserLoginRequest(
                any(UserLoginRequest.class),
                any(String.class),
                any(String.class)
            );
        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));
        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class), eq (UserType.TEACHER)))
            .thenReturn(new ArrayList<>(List.of(course)));

        TeacherProfile expectedTeacherProfile = new TeacherProfile(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new ArrayList<>(List.of(new CourseInfo(course.getCourseName(), course.getCourseDescription())))
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
            teacher.getEmail(),
            teacher.getPassword()
        );

        assertEquals(expectedTeacherProfile, teacherService.login(userLoginRequest));
    }

    @Test
    void registerShouldCreateNewTeacherEntityInDataBase() {
        doNothing()
            .when(requestValidator).
            validateUserRegistrationRequest(any(UserRegistrationRequest.class));

        doNothing()
            .when(teacherDao)
            .create(any(Teacher.class));

        when(passwordEncoder.encode(any(String.class)))
            .thenReturn("encodedString");


        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email",
            "pass",
            "pass",
            "fN",
            "lN"
        );

        teacherService.register(userRegistrationRequest);

        verify(passwordEncoder).encode(any(String.class));
        verify(requestValidator).validateUserRegistrationRequest(userRegistrationRequest);
        verify(teacherDao).create(any(Teacher.class));
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfTeacherInDataBase() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        doNothing()
            .when(teacherDao)
            .update(eq(teacher.getUserId()), any(Teacher.class));

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        teacherService.changePassword(passwordChangeRequest);

        verify(teacherDao).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(teacherDao).update(eq(teacher.getUserId()), any(Teacher.class));
    }

    @Test
    void deleteTeacherShouldRemoveTeacherFromDataBase() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(teacherDao)
            .deleteById(any(UUID.class));

        teacherService.deleteTeacher(teacher.getEmail());

        verify(teacherDao).findByEmail(teacher.getEmail());
        verify(teacherDao).deleteById(teacher.getUserId());
    }

    @Test
    void excludeTeacherFromCourseShouldRemoveTeacherFromChosenCourse() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(teacherDao)
            .excludeFromCourse(any(UUID.class), any(String.class));

        teacherService.excludeTeacherFromCourse(teacher.getEmail(), "cn");

        verify(teacherDao).findByEmail(teacher.getEmail());
        verify(teacherDao).excludeFromCourse(teacher.getUserId(), "cn");
    }

    @Test
    void addTeacherToCourseShouldAddTeacherToCourseByItsName() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass"
        );

        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(teacherDao)
            .addToCourse(any(UUID.class), any(String.class));

        teacherService.addTeacherToCourse(teacher.getEmail(), "cN");

        verify(teacherDao).findByEmail(teacher.getEmail());
        verify(teacherDao).addToCourse(teacher.getUserId(), "cN");
    }

}