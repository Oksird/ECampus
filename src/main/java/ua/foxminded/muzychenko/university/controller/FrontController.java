package ua.foxminded.muzychenko.university.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.university.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.university.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.university.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.university.service.AdminService;
import ua.foxminded.muzychenko.university.service.CourseService;
import ua.foxminded.muzychenko.university.service.GroupService;
import ua.foxminded.muzychenko.university.service.StudentService;
import ua.foxminded.muzychenko.university.service.TeacherService;
import ua.foxminded.muzychenko.university.service.validator.RequestValidator;
import ua.foxminded.muzychenko.university.view.ViewProvider;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FrontController {
    private static final String INVALID_CHOICE_MESSAGE = "Invalid choice";
    private static final String ENTER_COURSE_NAME_MESSAGE = "Enter course name";
    private static final String ENTER_TEACHERS_EMAIL_MESSAGE = "Enter teacher's email";
    private final ViewProvider viewProvider;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdminService adminService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final RequestValidator requestValidator;

    public void run() {
        boolean isRunning = true;
        while (isRunning) {
            displayMainMenu();
            int chooseDao = viewProvider.readInt();
            switch (chooseDao) {
                case 1 -> manageAdminDao();
                case 2 -> manageStudentDao();
                case 3 -> manageTeacherDao();
                case 4 -> manageCourseDao();
                case 5 -> manageGroupDao();
                case 0 -> isRunning = false;
                default -> viewProvider.printMessage(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void displayMainMenu() {
        viewProvider.printMessage("""
            Admin - 1
            Student - 2
            Teacher - 3
            Course - 4
            Group - 5
            Exit - 0
            """);
    }

    private void manageAdminDao() {
        while (true) {
            viewProvider.printMessage("""
                Register admin - 1
                Find admin by email - 2
                Find all admins - 3
                Back to main menu - 0
                """);
            int choose = viewProvider.readInt();
            switch (choose) {
                case 1 -> {
                    viewProvider.readString();
                    registerAdmin(getPreparedUserRegistrationRequest());
                    viewProvider.printMessage("User was registered");
                }
                case 2 -> {
                    viewProvider.printMessage("Enter admin email");
                    viewProvider.readString();
                    AdminProfile adminProfile = findAdminByEmail(viewProvider.readString());
                    viewProvider.printMessage(adminProfile.toString());
                }
                case 3 -> {
                    viewProvider.printMessage("Enter page number");
                    int pageNumber = viewProvider.readInt();
                    viewProvider.printMessage("Enter page size");
                    int pageSize = viewProvider.readInt();
                    List<AdminProfile> adminProfiles = findAllAdmins(pageNumber, pageSize);
                    adminProfiles.forEach(studentInfo -> System.out.println(studentInfo.toString()));
                }
                case 0 -> {
                    return;
                }
                default -> viewProvider.printMessage(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void manageStudentDao() {
        while (true) {
            viewProvider.printMessage("""
                Register student - 1
                Find student by email - 2
                Find all students - 3
                Add student to course - 4
                Add student to group - 5
                Back to main menu - 0
                """);
            int choose = viewProvider.readInt();
            switch (choose) {
                case 1 -> {
                    viewProvider.readString();
                    registerStudent(getPreparedUserRegistrationRequest());
                    viewProvider.printMessage("User was registered");
                }
                case 2 -> {
                    viewProvider.readString();
                    viewProvider.printMessage("Enter student's email");
                    StudentProfile studentProfile = findStudentByEmail(viewProvider.readString());
                    viewProvider.printMessage(studentProfile.toString());
                }
                case 3 -> {
                    viewProvider.printMessage("Enter page number");
                    int pageNumber = viewProvider.readInt();
                    viewProvider.printMessage("Enter page size");
                    int pageSize = viewProvider.readInt();
                    List<StudentProfile> studentProfiles = findAllStudents(pageNumber, pageSize);
                    studentProfiles.forEach(studentInfo -> System.out.println(studentInfo.toString()));
                }
                case 4 -> {
                    viewProvider.readString();
                    viewProvider.printMessage("Enter user email");
                    String userEmail = viewProvider.readString();
                    viewProvider.printMessage(ENTER_COURSE_NAME_MESSAGE);
                    String courseName = viewProvider.readString();
                    addStudentToCourse(userEmail, courseName);
                    viewProvider.printMessage("Student was successfully added to course " + courseName);
                }
                case 5 -> {
                    viewProvider.readString();
                    viewProvider.printMessage("Enter user email");
                    String userEmail1 = viewProvider.readString();
                    viewProvider.printMessage("Enter group name");
                    String groupName = viewProvider.readString();
                    addStudentToGroup(userEmail1, groupName);
                    viewProvider.printMessage("User was successfully added to group " + groupName);
                }
                case 0 -> {
                    return;
                }
                default -> viewProvider.printMessage(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void manageTeacherDao() {
        while (true) {
            viewProvider.printMessage("""
                Register teacher - 1
                Find teacher by email - 2
                Add teacher to course - 3
                Exclude teacher from course - 4
                Back to main menu - 0
                """);
            int choose = viewProvider.readInt();
            switch (choose) {
                case 1 -> {
                    viewProvider.readString();
                    registerTeacher(getPreparedUserRegistrationRequest());
                }
                case 2 -> {
                    viewProvider.readString();
                    viewProvider.printMessage(ENTER_TEACHERS_EMAIL_MESSAGE);
                    TeacherProfile teacherProfile = findTeacherByEmail(viewProvider.readString());
                    viewProvider.printMessage(teacherProfile.toString());
                }
                case 3 -> {
                    viewProvider.readString();
                    viewProvider.printMessage(ENTER_TEACHERS_EMAIL_MESSAGE);
                    String email = viewProvider.readString();
                    viewProvider.printMessage(ENTER_COURSE_NAME_MESSAGE);
                    String courseName = viewProvider.readString();
                    addTeacherToCourse(email, courseName);
                    viewProvider.printMessage("Teacher was added to course successfully");
                }
                case 4 -> {
                    viewProvider.readString();
                    viewProvider.printMessage(ENTER_TEACHERS_EMAIL_MESSAGE);
                    String teacherEmail = viewProvider.readString();
                    viewProvider.printMessage(ENTER_COURSE_NAME_MESSAGE);
                    String courseName1 = viewProvider.readString();
                    excludeTeacherFromCourse(teacherEmail, courseName1);
                    viewProvider.printMessage("Teacher was excluded from course successfully");
                }
                case 0 -> {
                    return;
                }
                default -> viewProvider.printMessage(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void manageCourseDao() {
        while (true) {
            viewProvider.printMessage("""
                Create courses - 1
                Delete course - 2
                Back to main menu - 0
                """);
            int choose = viewProvider.readInt();
            switch (choose) {
                case 1 -> {
                    viewProvider.readString();
                    viewProvider.printMessage(ENTER_COURSE_NAME_MESSAGE);
                    String courseName = viewProvider.readString();
                    viewProvider.printMessage("Enter course description");
                    String courseDescription = viewProvider.readString();
                    createCourse(new CourseInfo(courseName, courseDescription));
                }
                case 2 -> {
                    viewProvider.readString();
                    viewProvider.printMessage("Enter course name you want delete");
                    String courseName = viewProvider.readString();
                    deleteCourse(courseName);
                    viewProvider.printMessage("Course was deleted successfully");
                }
                case 0 -> {
                    return;
                }
                default -> viewProvider.printMessage(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void manageGroupDao() {
        while (true) {
            viewProvider.printMessage("""
                Create group - 1
                Delete group - 2
                Back to main menu - 0
                """);
            int choose = viewProvider.readInt();
            switch (choose) {
                case 1 -> {
                    viewProvider.readString();
                    viewProvider.printMessage("Enter group name you want create");
                    String groupName = viewProvider.readString();
                    createGroup(new GroupInfo(groupName));
                    viewProvider.printMessage("Group was created successfully");
                }
                case 2 -> {
                    viewProvider.readString();
                    viewProvider.printMessage("Enter group name you want delete");
                    String groupName1 = viewProvider.readString();
                    deleteGroup(groupName1);
                    viewProvider.printMessage("Group was deleted successfully");
                }
                case 0 -> {
                    return;
                }
                default -> viewProvider.printMessage(INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private void registerStudent(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        studentService.register(userRegistrationRequest);
    }

    private StudentProfile findStudentByEmail(String email) {
        return studentService.findStudentByEmail(email);
    }

    private List<StudentProfile> findAllStudents(Integer pageNumber, Integer pageSize) {
        return studentService.findAllStudents(pageNumber, pageSize);
    }


    private void addStudentToCourse(String studentEmail, String courseName) {
        studentService.addStudentToCourse(studentEmail, courseName);
    }

    private void addStudentToGroup(String studentEmail, String groupName) {
        studentService.addStudentToGroup(studentEmail, groupName);
    }

    private void registerAdmin(UserRegistrationRequest userRegistrationRequest) {
        adminService.register(userRegistrationRequest);
    }

    private UserRegistrationRequest getPreparedUserRegistrationRequest() {
        viewProvider.printMessage("Enter first name");
        String firstName = viewProvider.readString();
        viewProvider.printMessage("Enter last name");
        String lastName = viewProvider.readString();
        viewProvider.printMessage("Enter email");
        String email = viewProvider.readString();
        viewProvider.printMessage("Enter password");
        String password = viewProvider.readString();
        viewProvider.printMessage("Repeat password");
        String repeatPassword = viewProvider.readString();

        return new UserRegistrationRequest(
            email, password, repeatPassword, firstName, lastName
        );
    }

    private AdminProfile findAdminByEmail(String email) {
        return adminService.findAdminByEmail(email);
    }

    private List<AdminProfile> findAllAdmins(Integer pageNumber, Integer pageSize) {
        return adminService.findAllAdmins(pageNumber, pageSize);
    }

    private void registerTeacher(UserRegistrationRequest userRegistrationRequest) {
        teacherService.register(userRegistrationRequest);
    }

    private TeacherProfile findTeacherByEmail(String email) {
        return teacherService.findTeacherByEmail(email);
    }

    private void addTeacherToCourse(String email, String courseName) {
        teacherService.addTeacherToCourse(email, courseName);
    }

    private void excludeTeacherFromCourse(String email, String courseName) {
        teacherService.excludeTeacherFromCourse(email, courseName);
    }

    private void createCourse(CourseInfo courseInfo) {
        courseService.createCourse(courseInfo);
    }

    private void deleteCourse(String courseName) {
        courseService.deleteCourse(courseName);
    }

    private void createGroup(GroupInfo groupInfo) {
        groupService.createGroup(groupInfo);
    }

    private void deleteGroup(String groupName) {
        groupService.deleteGroup(groupName);
    }
}
