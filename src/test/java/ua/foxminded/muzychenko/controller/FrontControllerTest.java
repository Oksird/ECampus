package ua.foxminded.muzychenko.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.entity.Admin;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.entity.UserType;
import ua.foxminded.muzychenko.service.validator.RequestValidator;
import ua.foxminded.muzychenko.view.ViewProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(TestConfig.class)
class FrontControllerTest {

    @MockBean
    private ViewProvider viewProvider;
    @MockBean
    private StudentDao studentDao;
    @MockBean
    private CourseDao courseDao;
    @MockBean
    private GroupDao groupDao;
    @MockBean
    private TeacherDao teacherDao;
    @MockBean
    private AdminDao adminDao;
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
        doNothing().when(adminDao).create(any(Admin.class));
        frontController.run();
        verify(adminDao).create(any(Admin.class));
    }

    @Test
    void runShouldFindAdminByEmail() {
        Admin expectedAdmin = new Admin(UUID.randomUUID(), "fn", "ln", "email", "pass");
        when(viewProvider.readInt())
            .thenReturn(1)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");
        when(adminDao.findByEmail("email")).thenReturn(Optional.of(expectedAdmin));
        frontController.run();
        verify(adminDao).findByEmail(any(String.class));
        assertEquals(expectedAdmin, adminDao.findByEmail("email").orElse(null));
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
        when(viewProvider.readInt())
            .thenReturn(1)
            .thenReturn(3)
            .thenReturn(0);
        when(adminDao.findAll()).thenReturn(expectedAdmins);
        frontController.run();
        verify(adminDao).findAll();
        assertEquals(expectedAdmins, adminDao.findAll());
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
        doNothing().when(studentDao).create(any(Student.class));
        frontController.run();
        verify(studentDao).create(any(Student.class));
    }

    @Test
    void runShouldFindStudentByEmail() {
        Student expectedStudent = new Student(
            UUID.randomUUID(), "fn", "ln", "email", "pass", null);
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");
        when(studentDao.findByEmail("email")).thenReturn(Optional.of(expectedStudent));
        frontController.run();
        verify(studentDao).findByEmail(any(String.class));
        assertEquals(expectedStudent, studentDao.findByEmail("email").orElse(null));
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
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(3)
            .thenReturn(0);
        when(studentDao.findAll()).thenReturn(expectedStudents);
        frontController.run();
        verify(studentDao).findAll();
        assertEquals(expectedStudents, studentDao.findAll());
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

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(
                new Student(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "email",
                    "password",
                    null))
            );
        doNothing().when(studentDao).addToCourse(any(UUID.class), eq("courseName"));

        frontController.run();

        verify(studentDao).findByEmail(any(String.class));
        verify(studentDao).addToCourse(any(UUID.class), eq("courseName"));
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

        when(studentDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(
                new Student(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "email",
                    "password",
                    null)
                )
            );
        doNothing().when(studentDao).addToGroup(any(UUID.class), eq("groupName"));

        frontController.run();

        verify(studentDao).findByEmail(any(String.class));
        verify(studentDao).addToGroup(any(UUID.class), eq("groupName"));
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
        doNothing().when(teacherDao).create(any(Teacher.class));
        frontController.run();
        verify(teacherDao).create(any(Teacher.class));
    }

    @Test
    void runShouldFindTeacherByEmailWhenEmailIsCorrect() {
        Teacher expectedTeacher = new Teacher(UUID.randomUUID(), "fn", "ln", "email", "pass");
        when(viewProvider.readInt())
            .thenReturn(3)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");
        when(teacherDao.findByEmail("email")).thenReturn(Optional.of(expectedTeacher));
        frontController.run();
        verify(teacherDao).findByEmail(any(String.class));
        assertEquals(expectedTeacher, teacherDao.findByEmail("email").orElse(null));
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

        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(
                new Teacher(
                    UUID.randomUUID(),
                    "f",
                    "l",
                    "email",
                    "password"))
            );
        doNothing().when(teacherDao).addToCourse(any(UUID.class), eq("courseName"));

        frontController.run();

        verify(teacherDao).findByEmail(any(String.class));
        verify(teacherDao).addToCourse(any(UUID.class), eq("courseName"));
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
        when(teacherDao.findByEmail(any(String.class)))
            .thenReturn(Optional.of(
                new Teacher(
                    UUID.randomUUID(),
                    "fn",
                    "ln",
                    "email",
                    "pass")
                )
            );
        doNothing().when(teacherDao).excludeFromCourse(any(UUID.class), any(String.class));
        frontController.run();
        verify(teacherDao).findByEmail(any(String.class));
        verify(teacherDao).excludeFromCourse(any(UUID.class), any(String.class));
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
        doNothing().when(courseDao).create(any(Course.class));
        frontController.run();
        verify(courseDao).create(any(Course.class));
    }

    @Test
    void runShouldDeleteCourse() {
        when(viewProvider.readInt())
            .thenReturn(4)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("cName");
        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.of(
                new Course(
                    UUID.randomUUID(),
                    "cName",
                    "cDesc")
                )
            );
        doNothing().when(courseDao).deleteById(any(UUID.class));
        frontController.run();
        verify(courseDao).findByName(any(String.class));
        verify(courseDao).deleteById(any(UUID.class));
    }

    @Test
    void runShouldCreateGroup() {
        when(viewProvider.readInt())
            .thenReturn(5)
            .thenReturn(1)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("gName");
        doNothing().when(groupDao).create(any(Group.class));
        frontController.run();
        verify(groupDao).create(any(Group.class));
    }

    @Test
    void runShouldDeleteGroup() {
        when(viewProvider.readInt())
            .thenReturn(5)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("gName");
        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.of(
                new Group(
                    UUID.randomUUID(),
                    "gName"
                )
            ));
        doNothing().when(groupDao).deleteById(any(UUID.class));
        frontController.run();
        verify(groupDao).findByName(any(String.class));
        verify(groupDao).deleteById(any(UUID.class));
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

    @Test
    void runShouldFindTeacherByEmailAndCreateTeacherProfile() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "email",
            "pass"
        );
        Course course1 = new Course(UUID.randomUUID(), "cname1", "cdeesc");
        Course course2 = new Course(UUID.randomUUID(), "cname2", "cdewsc");

        when(viewProvider.readInt())
            .thenReturn(3)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");

        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class), eq(UserType.TEACHER)))
            .thenReturn(new ArrayList<>(List.of(course1, course2)));
        when(teacherDao.findByEmail(any(String.class))).thenReturn(Optional.of(teacher));

        frontController.run();

        verify(courseDao).findCoursesByUserIdAndUserType(any(UUID.class), eq(UserType.TEACHER));
        verify(teacherDao).findByEmail(("email"));
    }

    @Test
    void runShouldCreateStudentProfileWithPresentGroupInfoAndCourseInfo() {
        Group group = new Group(UUID.randomUUID(), "gName");
        Course course = new Course(UUID.randomUUID(), "cName", "cDesc");
        Student student = new Student(
            UUID.randomUUID(),
            "fName",
            "lName",
            "email",
            "pass",
            UUID.randomUUID()
        );
        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(3)
            .thenReturn(0);
        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class), eq(UserType.STUDENT)))
            .thenReturn(new ArrayList<>(List.of(course)));
        when(groupDao.findUsersGroup(any(UUID.class))).thenReturn(Optional.of(group));
        when(studentDao.findAll()).thenReturn(new ArrayList<>(List.of(student)));
        frontController.run();
        verify(groupDao).findUsersGroup(any(UUID.class));
        verify(studentDao).findAll();
        verify(courseDao).findCoursesByUserIdAndUserType(any(UUID.class), eq (UserType.STUDENT));
    }

    @Test
    void runShouldFindStudentByEmailAndCreateStudentProfileWithPresentGroupAndCourseInfo() {
        Group group = new Group(UUID.randomUUID(), "gName");
        Course course = new Course(UUID.randomUUID(), "cName", "cDesc");

        when(viewProvider.readInt())
            .thenReturn(2)
            .thenReturn(2)
            .thenReturn(0);
        when(viewProvider.readString())
            .thenReturn("email");

        when(groupDao.findUsersGroup(any(UUID.class))).thenReturn(Optional.of(group));
        when(courseDao.findCoursesByUserIdAndUserType(any(UUID.class), eq (UserType.STUDENT)))
            .thenReturn(new ArrayList<>(List.of(course)));
        when(studentDao.findByEmail(any(String.class))).thenReturn(
            Optional.of(
                new Student(
                    UUID.randomUUID(),
                    "fName",
                    "lName",
                    "email",
                    "pass",
                    group.getGroupId()
                )
            )
        );
        frontController.run();
        verify(groupDao).findUsersGroup(any(UUID.class));
        verify(courseDao).findCoursesByUserIdAndUserType(any(UUID.class), eq (UserType.STUDENT));
        verify(studentDao).findByEmail(any(String.class));
    }
}
