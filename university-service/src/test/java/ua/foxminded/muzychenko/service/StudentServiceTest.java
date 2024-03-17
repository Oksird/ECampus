package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.PendingUser;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.GroupRepository;
import ua.foxminded.muzychenko.repository.PendingUserRepository;
import ua.foxminded.muzychenko.repository.StudentRepository;
import ua.foxminded.muzychenko.service.mapper.StudentProfileMapper;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = StudentService.class)
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
    private StudentProfileMapper studentProfileMapper;
    @MockBean
    private PendingUserRepository pendingUserRepository;
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
            null,
            "380227738888",
            "address"
        );

        StudentProfile studentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo("str" ,"gn", 1, null),
            "380227738888",
            "address"
            );

        when(studentRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(student));

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(new Group(UUID.randomUUID(), "gn")));

        when(studentProfileMapper.mapStudentInfoToProfile(any(Student.class)))
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
                null,
                "380227738888",
                "address"
            ),
            new Student(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                null,
                "380227738888",
                "address"
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
            "380227738888",
            "address"
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getUserId().toString(),
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            "380227738888",
            "address"
        );

        Set<Course> courseList = new HashSet<>();

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        when(studentRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(studentList));

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.empty());

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student1)))
            .thenReturn(studentProfile1);

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student2)))
            .thenReturn(studentProfile2);

        assertEquals(studentProfileList, studentService.findAll(1,1).getContent());
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
                null,
                "380227738888",
                "address"
            ),
            new Student(
                UUID.randomUUID(),
                "fn2",
                "ln2",
                "em2",
                "pass2",
                null,
                "380227738888",
                "address"
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
            "380227738888",
            "address"
        );

        StudentProfile studentProfile2 = new StudentProfile(
            student2.getUserId().toString(),
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            "380227738888",
            "address"
        );

        List<StudentProfile> studentProfileList = new ArrayList<>(List.of(studentProfile1, studentProfile2));

        Set<Course> courseList = new HashSet<>();

        when(studentRepository.findByGroup_GroupName(any(String.class)))
            .thenReturn(studentList);

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.empty());

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student1)))
            .thenReturn(studentProfile1);

        when(studentProfileMapper.mapStudentInfoToProfile(eq (student2)))
            .thenReturn(studentProfile2);

        assertEquals(studentProfileList, studentService.findStudentsByGroup("gn"));
    }

    @Test
    void changePasswordShouldUpdatePasswordFieldOfStudentInDataBase() {
        Student student = new Student(
            UUID.randomUUID(),
            "fN",
            "lN",
            "em",
            "pass",
            null,
            "380227738888",
            "address"
        );

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        doNothing()
            .when(requestValidator)
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
        verify(requestValidator).validatePasswordChangeRequest(passwordChangeRequest);
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
            null,
            "380227738888",
            "address"
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
            null,
            "380227738888",
            "address"
        );

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        studentService.excludeStudentFromGroup(student.getEmail());

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
            null,
            "380227738888",
            "address"
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
            group,
            "380227738888",
            "address"
        );

        when(groupRepository.findUsersGroup(any(UUID.class)))
            .thenReturn(Optional.of(group));

        StudentProfile studentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            new GroupInfo(group.getGroupId().toString() ,group.getGroupName(), 1, null),
            "380227738888",
            "address"
        );

        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(student));

        when(studentProfileMapper.mapStudentInfoToProfile(student))
            .thenReturn(studentProfile);

        assertEquals(studentProfile, studentService.findStudentByEmail("email"));
    }

    @Test
    void findStudentByEmailShouldThrowExceptionWhenEmailIsWrong() {
        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> studentService.findStudentByEmail("email"));
    }

    @Test
    void createStudentFromPendingUserShouldCreateNewStudentFromPUserInfo() {
        PendingUser pendingUser = new PendingUser(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380227738888",
            "address"
        );

        PendingUserProfile pendingUserProfile = new PendingUserProfile(
            pendingUser.getUserId().toString(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        Student student = new Student(
            UUID.randomUUID(),
            pendingUser.getFirstName(),
            pendingUser.getLastName(),
            pendingUser.getEmail(),
            pendingUser.getPassword(),
            null,
            pendingUser.getPhoneNumber(),
            pendingUser.getAddress()
        );

        when(pendingUserRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(pendingUser));

        doNothing().when(pendingUserRepository).delete(pendingUser);

        when(studentRepository.save(student)).thenReturn(student);

        studentService.createStudentFromPendingUser(pendingUserProfile);

        assertEquals(pendingUserRepository.findById(UUID.randomUUID()).get(), pendingUser);
        verify(pendingUserRepository).delete(pendingUser);
        assertEquals(studentRepository.save(student), student);
    }
}
