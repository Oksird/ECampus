package ua.foxminded.muzychenko.university.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.dao.CourseRepository;
import ua.foxminded.muzychenko.university.dao.GroupRepository;
import ua.foxminded.muzychenko.university.dao.StudentRepository;
import ua.foxminded.muzychenko.university.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.university.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.university.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.university.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.university.entity.Course;
import ua.foxminded.muzychenko.university.entity.Group;
import ua.foxminded.muzychenko.university.entity.Student;
import ua.foxminded.muzychenko.university.service.mapper.StudentProfileMapper;
import ua.foxminded.muzychenko.university.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.university.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.university.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestUniversityApplication.class)
class StudentServiceTest {

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private GroupRepository groupRepository;
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
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo("str" ,"gn"),
            new HashSet<>()
            );

        when(studentRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(student));

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(new Group(UUID.randomUUID(), "gn")));

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(new HashSet<>());

        when(studentProfileMapper.mapStudentInfoToProfile(any(Student.class), any(Group.class), eq (new HashSet<>())))
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
            student1.getUserId().toString(),
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new HashSet<>()
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getUserId().toString(),
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new HashSet<>()
        );

        Set<Course> courseList = new HashSet<>();

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        when(studentRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(studentList));

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(courseList);

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.empty());

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student1), any(), eq(courseList)))
            .thenReturn(studentProfile1);

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student2), any(), eq(courseList)))
            .thenReturn(studentProfile2);

        assertEquals(studentProfileList, studentService.findAll(1,1).getContent());
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
            student1.getUserId().toString(),
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new HashSet<>()
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getUserId().toString(),
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new HashSet<>()
        );

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        Set<Course> courseList = new HashSet<>();

        when(studentRepository.findByCourses_CourseName(any(String.class)))
            .thenReturn(studentList);

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(courseList);

        when(groupRepository.findUsersGroup(any(UUID.class)))
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
            student1.getUserId().toString(),
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new HashSet<>()
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getUserId().toString(),
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new HashSet<>()
        );

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        Set<Course> courseList = new HashSet<>();

        when(studentRepository.findByGroup_GroupName(any(String.class)))
            .thenReturn(studentList);

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(courseList);

        when(groupRepository.findUsersGroup(any(UUID.class)))
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

        UUID groupId = UUID.randomUUID();

        when(studentRepository.save(any(Student.class)))
            .thenReturn(student);

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
        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));
        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(new HashSet<>(List.of(course)));
        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(new Group(groupId, "gn")));

        StudentProfile expectedStudentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo(groupId.toString() ,"gn"),
            new HashSet<>(List.of(new CourseInfo(course.getCourseId().toString(), course.getCourseName(), course.getCourseDescription())))
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

        when(studentRepository.save(any(Student.class)))
            .thenReturn(new Student());

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
        verify(studentRepository).save(any(Student.class));
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

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        studentService.changePassword(passwordChangeRequest);

        verify(studentRepository).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(studentRepository).save(any(Student.class));
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

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(studentRepository)
            .deleteById(any(UUID.class));

        studentService.deleteStudent(student.getEmail());

        verify(studentRepository).findByEmail(student.getEmail());
        verify(studentRepository).deleteById(student.getUserId());
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

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        studentService.excludeStudentFromGroup(student.getEmail());

        verify(studentRepository).findByEmail(student.getEmail());
        verify(studentRepository).save(any(Student.class));
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

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(new Course(UUID.randomUUID(), "cn", "cd")));

        studentService.excludeStudentFromCourse(student.getEmail(), "cn");

        verify(studentRepository).findByEmail(student.getEmail());
        verify(courseRepository).findByCourseName("cn");
        verify(studentRepository).save(any(Student.class));
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

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(new Course(UUID.randomUUID(), "cn", "cd")));

        studentService.addStudentToCourse(student.getEmail(), "cn");

        verify(studentRepository).findByEmail(student.getEmail());
        verify(studentRepository).save(any(Student.class));
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

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(new Group(UUID.randomUUID(), "gN")));

        studentService.addStudentToGroup(student.getEmail(), "gN");

        verify(studentRepository).findByEmail(student.getEmail());
        verify(studentRepository).save(any(Student.class));
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
            course1.getCourseId().toString(),
            course1.getCourseName(),
            course1.getCourseDescription()
        );
        CourseInfo courseInfo2 = new CourseInfo(
            course2.getCourseId().toString(),
            course2.getCourseName(),
            course2.getCourseDescription()
        );

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(group));

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(new HashSet<>(List.of(course1, course2)));

        StudentProfile studentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo(group.getGroupId().toString() ,group.getGroupName()),
            new HashSet<>(List.of(courseInfo1, courseInfo2))
        );

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        when(studentProfileMapper.mapStudentInfoToProfile(student, group, new HashSet<>(List.of(course1, course2))))
            .thenReturn(studentProfile);

        assertEquals(studentProfile, studentService.findStudentByEmail("email"));
    }

    @Test
    void findStudentByEmailShouldThrowExceptionWhenEmailIsWrong() {
        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> studentService.findStudentByEmail("email"));
    }
}
