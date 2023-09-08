package ua.foxminded.muzychenko.university.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.university.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.university.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.university.entity.Admin;
import ua.foxminded.muzychenko.university.entity.Student;
import ua.foxminded.muzychenko.university.service.AdminService;
import ua.foxminded.muzychenko.university.service.CourseService;
import ua.foxminded.muzychenko.university.service.GroupService;
import ua.foxminded.muzychenko.university.service.StudentService;
import ua.foxminded.muzychenko.university.service.TeacherService;
import ua.foxminded.muzychenko.university.service.validator.RequestValidator;
import ua.foxminded.muzychenko.university.view.ViewProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringJUnitConfig(TestUniversityApplication.class)
class FrontControllerTest {

    @MockBean
    private ViewProvider viewProvider;
    @MockBean
    private StudentService studentService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private AdminService adminService;
    @MockBean
    private RequestValidator requestValidator;
    @Autowired
    private FrontController frontController;

    @Test
    void runShouldCreateNewAdminWhenAllFieldsAreCorrect() {
        when(viewProvider.readInt())
            .thenReturn(1)
            .thenReturn(1)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("Testname")
            .thenReturn("Testsurname")
            .thenReturn("email@test.com")
            .thenReturn("2837u47J!")
            .thenReturn("2837u47J!");
        doNothing().when(adminService).register(any(UserRegistrationRequest.class));
        frontController.run();
        verify(adminService).register(any(UserRegistrationRequest.class));
    }

    @Test
    void runShouldFindAdminByEmail() {
        when(viewProvider.readInt())
            .thenReturn(1)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");

        AdminProfile adminProfile = new AdminProfile(
            "fn",
            "ln",
            "em"
        );

        when(adminService.findAdminByEmail("email")).thenReturn(adminProfile);
        frontController.run();
        verify(adminService).findAdminByEmail(any(String.class));
        assertEquals(adminProfile, adminService.findAdminByEmail("email"));
    }

    @Test
    void runShouldReturnAllAdmins() {
        List<Admin> expectedAdmins = new ArrayList<>(
            List.of(
                new Admin(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "e",
                    "p"
                ),
                new Admin(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "e",
                    "p"
                )));

        List<AdminProfile> adminProfileList = new ArrayList<>(expectedAdmins.size());

        expectedAdmins.forEach(
            admin -> adminProfileList.add(
                new AdminProfile(
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getEmail()
                )
            )
        );

        when(viewProvider.readInt())
            .thenReturn(1)
            .thenReturn(3)
            .thenReturn(1)
            .thenReturn(1)
            .thenReturn(0);
        when(adminService.findAllAdmins(1, 1)).thenReturn(adminProfileList);
        frontController.run();
        verify(adminService).findAllAdmins(any(), any());
        assertEquals(adminProfileList, adminService.findAllAdmins(1, 1));
    }

    @Test
    void runShouldRegisterStudent() {
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(1)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("Testname")
            .thenReturn("Testsurname")
            .thenReturn("email@mail.com")
            .thenReturn("8273W99w!")
            .thenReturn("8273W99w!");
        doNothing().when(studentService).register(any(UserRegistrationRequest.class));
        frontController.run();
        verify(studentService).register(any(UserRegistrationRequest.class));
    }

    @Test
    void runShouldFindStudentByEmail() {
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");

        StudentProfile studentProfile = new StudentProfile(
            "fn",
            "ln",
            "em",
            null,
            new HashSet<>()
        );

        when(studentService.findStudentByEmail(any(String.class)))
            .thenReturn(studentProfile);

        frontController.run();

        assertEquals(studentProfile, studentService.findStudentByEmail("email"));
    }

    @Test
    void runShouldReturnAllStudents() {
        List<Student> expectedStudents = new ArrayList<>(
            List.of(
                new Student(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "e",
                    "p",
                    null
                ),
                new Student(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "e",
                    "p",
                    null
                )));

        Student student1 = expectedStudents.get(0);
        Student student2 = expectedStudents.get(1);

        StudentProfile studentProfile1 = new StudentProfile(
            student1.getFirstName(),
            student1.getLastName(),
            student1.getEmail(),
            null,
            new HashSet<>()
        );
        StudentProfile studentProfile2 = new StudentProfile(
            student2.getFirstName(),
            student2.getLastName(),
            student2.getEmail(),
            null,
            new HashSet<>()
        );

        List<StudentProfile> studentProfileList = List.of(studentProfile1, studentProfile2);

        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(3)
            .thenReturn(1)
            .thenReturn(1)
            .thenReturn(0);
        when(studentService.findAllStudents(any(), any())).thenReturn(studentProfileList);
        frontController.run();
        verify(studentService).findAllStudents(1, 1);
        assertEquals(studentProfileList, studentService.findAllStudents(1, 1));
    }

    @Test
    void runShouldAddStudentToCourse() {
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(4)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email")
            .thenReturn("courseName");

        doNothing().when(studentService).addStudentToCourse(any(String.class), eq("courseName"));

        frontController.run();

        verify(studentService).addStudentToCourse(any(String.class), eq("courseName"));
    }

    @Test
    void runShouldAddStudentToGroup() {
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(5)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email")
            .thenReturn("groupName");

        doNothing().when(studentService).addStudentToGroup(any(String.class), eq("groupName"));

        frontController.run();

        verify(studentService).addStudentToGroup(any(String.class), eq("groupName"));
    }

    @Test
    void runShouldCreateNewTeacher() {
        when(viewProvider.readInt())
            .thenReturn(3)
            .thenReturn(1)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("Testname")
            .thenReturn("Testsurname")
            .thenReturn("mailtest@test.com")
            .thenReturn("2837u47J!")
            .thenReturn("2837u47J!");
        doNothing().when(teacherService).register(any(UserRegistrationRequest.class));
        frontController.run();
        verify(teacherService).register(any(UserRegistrationRequest.class));
    }

    @Test
    void runShouldFindTeacherByEmailWhenEmailIsCorrect() {
        when(viewProvider.readInt())
            .thenReturn(3)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");

        TeacherProfile teacherProfile = new TeacherProfile(
            "fn",
            "ln",
            "em",
            new HashSet<>()
        );

        when(teacherService.findTeacherByEmail(any(String.class))).thenReturn(teacherProfile);
        frontController.run();
        verify(teacherService).findTeacherByEmail(any(String.class));
        assertEquals(teacherProfile, teacherService.findTeacherByEmail("email"));
    }

    @Test
    void runShouldAddTeacherToCourse() {
        when(viewProvider.readInt())
            .thenReturn(3)
            .thenReturn(3)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email")
            .thenReturn("courseName");

        doNothing().when(teacherService).addTeacherToCourse(any(String.class), eq("courseName"));

        frontController.run();

        verify(teacherService).addTeacherToCourse(any(String.class), eq("courseName"));
    }

    @Test
    void runShouldExcludeTeacherFromCourse() {
        when(viewProvider.readInt())
            .thenReturn(3)
            .thenReturn(4)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email")
            .thenReturn("courseName");

        doNothing().when(teacherService).excludeTeacherFromCourse(any(String.class), any(String.class));
        frontController.run();
        verify(teacherService).excludeTeacherFromCourse(any(String.class), any(String.class));
    }

    @Test
    void runShouldCreateNewCourse() {
        when(viewProvider.readInt())
            .thenReturn(4)
            .thenReturn(1)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("cName")
            .thenReturn("cDesc");
        doNothing().when(courseService).createCourse(any(CourseInfo.class));
        frontController.run();
        verify(courseService).createCourse(any(CourseInfo.class));
    }

    @Test
    void runShouldDeleteCourse() {
        when(viewProvider.readInt())
            .thenReturn(4)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("cName");

        doNothing().when(courseService).deleteCourse(any(String.class));

        frontController.run();

        verify(courseService).deleteCourse(any(String.class));
    }

    @Test
    void runShouldCreateGroup() {
        when(viewProvider.readInt())
            .thenReturn(5)
            .thenReturn(1)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("gName");

        doNothing().when(groupService).createGroup(any(GroupInfo.class));

        frontController.run();

        verify(groupService).createGroup(any(GroupInfo.class));
    }

    @Test
    void runShouldDeleteGroup() {
        when(viewProvider.readInt())
            .thenReturn(5)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("gName");

        doNothing().when(groupService).deleteGroup(any(String.class));

        frontController.run();

        verify(groupService).deleteGroup(any(String.class));
    }

    @Test
    void runShouldPrintInvalidChoiceMessageWhenChoiceIsIncorrect() {
        when(viewProvider.readInt())
            .thenReturn(9)
            .thenReturn(0);
        doNothing().when(viewProvider).printMessage("Invalid choice");
        frontController.run();
        verify(viewProvider).printMessage("Invalid choice");
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "2",
        "3",
        "4",
        "5"
    })
    void runWillPrintInvalidChoiceMessageWhenUserEnteredWrongNumber(int menuChoice) {
        when(viewProvider.readInt())
            .thenReturn(menuChoice)
            .thenReturn(9)
            .thenReturn(0);
        doNothing().when(viewProvider).printMessage("Invalid choice");

        frontController.run();

        verify(viewProvider).printMessage("Invalid choice");
    }
}
