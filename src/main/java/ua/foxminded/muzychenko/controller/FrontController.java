package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.dao.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.dao.exception.UserNotFoundException;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FrontController {
    private static final String INVALID_CHOICE_MESSAGE = "Invalid choice";
    private static final String ENTER_COURSE_NAME_MESSAGE = "Enter course name";
    private static final String ENTER_TEACHERS_EMAIL_MESSAGE = "Enter teacher's email";
    private final ViewProvider viewProvider;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final AdminDao adminDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
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
                    List<AdminProfile> adminProfiles = findAllAdmins();
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
                    List<StudentProfile> studentProfiles = findAllStudents();
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
        studentDao.create(
            new Student(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPassword(),
                null
            )
        );
    }

    private StudentProfile findStudentByEmail(String email) {
        Student student = studentDao.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        GroupInfo groupInfo = groupDao.findUsersGroup(student.getUserId())
            .map(group -> new GroupInfo(group.getGroupName()))
            .orElse(null);

        List<Course> studentCourses = courseDao.findCoursesByUserIdAndUserType(student.getUserId(), UserType.STUDENT);
        List<CourseInfo> courseInfoList = studentCourses.stream()
            .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
            .toList();

        return new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            courseInfoList
        );
    }

    private List<StudentProfile> findAllStudents() {
        List<Student> students = studentDao.findAll();
        List<StudentProfile> studentProfiles = new ArrayList<>();

        for (Student student : students) {
            UUID userId = student.getUserId();

            GroupInfo groupInfo = groupDao.findUsersGroup(userId)
                .map(group -> new GroupInfo(group.getGroupName()))
                .orElse(null);

            List<Course> studentCourses = courseDao.findCoursesByUserIdAndUserType(userId, UserType.STUDENT);
            List<CourseInfo> courseInfoList = studentCourses.stream()
                .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
                .toList();

            studentProfiles.add(new StudentProfile(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                groupInfo,
                courseInfoList
            ));
        }

        return studentProfiles;
    }



    private void addStudentToCourse(String studentEmail, String courseName) {
        Student student =
            studentDao.findByEmail(studentEmail).orElseThrow(UserNotFoundException::new);
        studentDao.addToCourse(student.getUserId(), courseName);
    }

    private void addStudentToGroup(String studentEmail, String groupName) {
        Student student =
            studentDao.findByEmail(studentEmail).orElseThrow(UserNotFoundException::new);
        studentDao.addToGroup(student.getUserId(), groupName);
    }

    private void registerAdmin(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        adminDao.create(
            new Admin(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPassword()
            )
        );
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
        Admin admin = adminDao.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        return new AdminProfile(
            admin.getFirstName(),
            admin.getLastName(),
            admin.getEmail()
        );
    }

    private List<AdminProfile> findAllAdmins() {
        List<Admin> admins = adminDao.findAll();
        List<AdminProfile> adminProfiles = new ArrayList<>();
        admins.forEach(
            student -> adminProfiles.add(
                new AdminProfile(
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail()
                )));
        return adminProfiles;
    }

    private void registerTeacher(UserRegistrationRequest userRegistrationRequest) {
        requestValidator.validateUserRegistrationRequest(userRegistrationRequest);
        teacherDao.create(
            new Teacher(
                UUID.randomUUID(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getPassword()
            )
        );
    }

    private TeacherProfile findTeacherByEmail(String email) {
        Teacher teacher = teacherDao.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        List<Course> coursesList = courseDao.findCoursesByUserIdAndUserType(teacher.getUserId(), UserType.TEACHER);
        List<CourseInfo> courseInfoList = new ArrayList<>();
        coursesList.forEach(
            course -> courseInfoList
                .add(new CourseInfo(
                    course.getCourseName(),
                    course.getCourseDescription())
                )
        );
        return new TeacherProfile(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseInfoList);
    }

    private void addTeacherToCourse(String email, String courseName) {
        Teacher teacher = teacherDao.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        teacherDao.addToCourse(teacher.getUserId(), courseName);
    }

    private void excludeTeacherFromCourse(String email, String courseName) {
        Teacher teacher = teacherDao.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        teacherDao.excludeFromCourse(teacher.getUserId(), courseName);
    }

    private void createCourse(CourseInfo courseInfo) {
        courseDao.create(new Course(UUID.randomUUID(), courseInfo.getCourseName(), courseInfo.getCourseDescription()));
    }

    private void deleteCourse(String courseName) {
        courseDao.deleteById(courseDao.findByName(courseName).orElseThrow(CourseNotFoundException::new).getCourseId());
    }

    private void createGroup(GroupInfo groupInfo) {
        Group group = new Group(UUID.randomUUID(), groupInfo.getGroupName());
        groupDao.create(group);
    }

    private void deleteGroup(String groupName) {
        groupDao.deleteById(groupDao.findByName(groupName).orElseThrow(GroupNotFoundException::new).getGroupId());
    }
}
