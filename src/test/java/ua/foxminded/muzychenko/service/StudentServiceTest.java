package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.service.mapper.StudentProfileMapper;
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
    @MockBean
    private StudentProfileMapper studentProfileMapper;
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

        StudentProfile studentProfile = new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo("gn"),
            new ArrayList<>()
            );

        when(studentDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(student));

        when(groupDao.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(new Group(UUID.randomUUID(), "gn")));

        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class)))
            .thenReturn(new ArrayList<>());

        when(studentProfileMapper.mapStudentInfoToProfile(any(Student.class), any(Group.class), eq (new ArrayList<>())))
            .thenReturn(studentProfile);

        assertEquals(studentProfile, studentService.findStudentById(UUID.randomUUID()));
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

        Student student1 = studentList.get(0);
        Student student2 = studentList.get(1);

        StudentProfile studentProfile1 = new StudentProfile(
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new ArrayList<>()
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new ArrayList<>()
        );

        List<Course> courseList = new ArrayList<>();

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        when(studentDao.findAll(any(Long.class), any(Long.class)))
            .thenReturn(studentList);

        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class)))
            .thenReturn(courseList);

        when(groupDao.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.empty());

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student1), any(), eq(courseList)))
            .thenReturn(studentProfile1);

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student2), any(), eq(courseList)))
            .thenReturn(studentProfile2);

        assertEquals(studentProfileList, studentService.findAllStudents(1L,1L));
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

        Student student1 = studentList.get(0);
        Student student2 = studentList.get(1);

        StudentProfile studentProfile1 = new StudentProfile(
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new ArrayList<>()
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new ArrayList<>()
        );

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        List<Course> courseList = new ArrayList<>();

        when(studentDao.findByCourse(any(String.class)))
            .thenReturn(studentList);

        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class)))
            .thenReturn(courseList);

        when(groupDao.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.empty());

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student1), any(), eq(courseList)))
            .thenReturn(studentProfile1);

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student2), any(), eq(courseList)))
            .thenReturn(studentProfile2);

        assertEquals(studentProfileList, studentService.findStudentsByCourse("cn"));
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

        Student student1 = studentList.get(0);
        Student student2 = studentList.get(1);

        StudentProfile studentProfile1 = new StudentProfile(
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new ArrayList<>()
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new ArrayList<>()
        );

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        List<Course> courseList = new ArrayList<>();

        when(studentDao.findByGroup(any(String.class)))
            .thenReturn(studentList);

        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class)))
            .thenReturn(courseList);

        when(groupDao.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.empty());

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student1), any(), eq(courseList)))
            .thenReturn(studentProfile1);

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student2), any(), eq(courseList)))
            .thenReturn(studentProfile2);

        assertEquals(studentProfileList, studentService.findStudentsByGroup("gn"));
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
        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class)))
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
            .excludeFromGroup(any(UUID.class));

        studentService.excludeStudentFromGroup(student.getEmail());

        verify(studentDao).findByEmail(student.getEmail());
        verify(studentDao).excludeFromGroup(student.getUserId());
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

    @Test
    void findStudentByEmailShouldReturnStudent() {
        Group group = new Group(UUID.randomUUID(), "GN");

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            group
        );

        Course course1 = new Course(UUID.randomUUID(), "cn1", "cd1");
        Course course2 = new Course(UUID.randomUUID(), "cn2", "cd2");

        CourseInfo courseInfo1 = new CourseInfo(
            course1.getCourseName(),
            course1.getCourseDescription()
        );
        CourseInfo courseInfo2 = new CourseInfo(
            course2.getCourseName(),
            course2.getCourseDescription()
        );

        when(groupDao.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(group));

        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class)))
            .thenReturn(new ArrayList<>(List.of(course1, course2)));

        StudentProfile studentProfile = new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo(group.getGroupName()),
            new ArrayList<>(List.of(courseInfo1, courseInfo2))
        );

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        when(studentProfileMapper.mapStudentInfoToProfile(student, group, new ArrayList<>(List.of(course1, course2))))
            .thenReturn(studentProfile);

        assertEquals(studentProfile, studentService.findStudentByEmail("email"));
    }

    @Test
    void findStudentByEmailShouldThrowExceptionWhenEmailIsWrong() {
        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> studentService.findStudentByEmail("email"));
    }
}