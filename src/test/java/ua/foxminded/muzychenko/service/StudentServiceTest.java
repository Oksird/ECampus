package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.entity.UserType;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class StudentServiceTest {

    @MockBean
    private StudentDao studentDao;
    @MockBean
    private CourseDao courseDao;
    @MockBean
    private GroupDao groupDao;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PasswordValidator passwordValidator;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentService studentService;

    @Test
    void findStudentByIdShouldReturnStudentWithCorrectID() {
        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            null
        );

        when(studentDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(student));
        assertEquals(student, studentService.findStudentById(UUID.randomUUID()));
    }

    @Test
    void findAllStudentsShouldReturnListOfAllStudentsByPage() {
        List<Student> studentList = new ArrayList<>(List.of(
            new Student(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass",
                null
            ),
            new Student(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                null
            )
        ));

        when(studentDao.findAll(any(Long.class), any(Long.class)))
            .thenReturn(studentList);

        assertEquals(studentList, studentService.findAllStudents(1L,1L));
    }

    @Test
    void findStudentsByCourseShouldReturnAllStudentsRelatedToCourse() {
        List<Student> studentList = new ArrayList<>(List.of(
            new Student(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass",
                null
            ),
            new Student(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                null
            )
        ));

        when(studentDao.findByCourse(any(String.class)))
            .thenReturn(studentList);

        assertEquals(studentList, studentService.findStudentsByCourse("cn"));
    }

    @Test
    void findStudentsByGroupShouldReturnAllStudentsRelatedToGroup() {
        List<Student> studentList = new ArrayList<>(List.of(
            new Student(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass",
                null
            ),
            new Student(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                null
            )
        ));

        when(studentDao.findByGroup(any(String.class)))
            .thenReturn(studentList);

        assertEquals(studentList, studentService.findStudentsByGroup("gn"));
    }

    @Test
    void loginShouldReturnCorrectUserProfile() {

        Course course = new Course(UUID.randomUUID(), "cName", "cDesc");

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            null
        );
        doNothing()
            .when(studentDao)
            .create(any(Student.class)
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
        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));
        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class), eq (UserType.STUDENT)))
            .thenReturn(new ArrayList<>(List.of(course)));
        when(groupDao.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(new Group(UUID.randomUUID(), "gn")));

        StudentProfile expectedStudentProfile = new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo("gn"),
            new ArrayList<>(List.of(new CourseInfo(course.getCourseName(), course.getCourseDescription())))
        );

        UserLoginRequest userLoginRequest = new UserLoginRequest(
            student.getEmail(),
            student.getPassword()
        );

        assertEquals(expectedStudentProfile, studentService.login(userLoginRequest));
    }

    @Test
    void registerShouldCreateNewStudentEntityInDataBase() {
        doNothing()
            .when(requestValidator).
            validateUserRegistrationRequest(any(UserRegistrationRequest.class));

        doNothing()
            .when(studentDao)
            .create(any(Student.class));

        when(passwordEncoder.encode(any(String.class)))
            .thenReturn("encodedString");


        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
            "email",
            "pass",
            "pass",
            "fN",
            "lN"
        );

        studentService.register(userRegistrationRequest);

        verify(passwordEncoder).encode(any(String.class));
        verify(requestValidator).validateUserRegistrationRequest(userRegistrationRequest);
        verify(studentDao).create(any(Student.class));
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfStudentInDataBase() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        doNothing()
            .when(studentDao)
            .update(eq(student.getUserId()), any(Student.class));

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        studentService.changePassword(passwordChangeRequest);

        verify(studentDao).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(studentDao).update(eq(student.getUserId()), any(Student.class));
    }

    @Test
    void deleteStudentShouldRemoveStudentFromDataBase() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(studentDao)
            .deleteById(any(UUID.class));

        studentService.deleteStudent(student.getEmail());

        verify(studentDao).findByEmail(student.getEmail());
        verify(studentDao).deleteById(student.getUserId());
    }

    @Test
    void excludeStudentFromGroupShouldExcludeStudentFromChosenGroup() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(studentDao)
            .excludeFromGroup(any(UUID.class), any(String.class));

        studentService.excludeStudentFromGroup(student.getEmail(), "gN");

        verify(studentDao).findByEmail(student.getEmail());
        verify(studentDao).excludeFromGroup(student.getUserId(), "gN");
    }

    @Test
    void excludeStudentFromCourseShouldRemoveStudentFromChosenCourse() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(studentDao)
            .excludeFromCourse(any(UUID.class), any(String.class));

        studentService.excludeStudentFromCourse(student.getEmail(), "cn");

        verify(studentDao).findByEmail(student.getEmail());
        verify(studentDao).excludeFromCourse(student.getUserId(), "cn");
    }

    @Test
    void addStudentToCourseShouldAddStudentToCourseByItsName() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(studentDao)
            .addToCourse(any(UUID.class), any(String.class));

        studentService.addStudentToCourse(student.getEmail(), "cN");

        verify(studentDao).findByEmail(student.getEmail());
        verify(studentDao).addToCourse(student.getUserId(), "cN");
    }

    @Test
    void addStudentToGroupShouldAddStudentToGroupByItsName() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(studentDao)
            .addToGroup(any(UUID.class), any(String.class));

        studentService.addStudentToGroup(student.getEmail(), "gN");

        verify(studentDao).findByEmail(student.getEmail());
        verify(studentDao).addToGroup(student.getUserId(), "gN");
    }
}