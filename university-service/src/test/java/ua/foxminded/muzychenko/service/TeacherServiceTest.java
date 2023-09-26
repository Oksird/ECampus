package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.dto.request.PasswordChangeRequest;
import ua.foxminded.muzychenko.dto.request.UserLoginRequest;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.service.mapper.TeacherProfileMapper;
import ua.foxminded.muzychenko.service.util.PasswordEncoder;
import ua.foxminded.muzychenko.service.validator.PasswordValidator;
import ua.foxminded.muzychenko.service.validator.RequestValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
classes = TeacherService.class)
class TeacherServiceTest {
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private RequestValidator requestValidator;
    @MockBean
    private PasswordValidator passwordValidator;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private TeacherProfileMapper teacherProfileMapper;
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

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new HashSet<>());

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(new HashSet<>());

        when(teacherRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(teacher));

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher), any()))
            .thenReturn(teacherProfile);

        assertEquals(teacherProfile, teacherService.findTeacherById(UUID.randomUUID()));
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

        Teacher teacher1 = teacherList.get(0);
        Teacher teacher2 = teacherList.get(1);

        TeacherProfile teacherProfile1 = new TeacherProfile(
            teacher1.getUserId().toString(),
            teacher1.getFirstName(),
            teacher1.getLastName(),
            teacher1.getEmail(),
            new HashSet<>()
        );
        TeacherProfile teacherProfile2 = new TeacherProfile(
            teacher2.getUserId().toString(),
            teacher2.getFirstName(),
            teacher2.getLastName(),
            teacher2.getEmail(),
            new HashSet<>()
        );

        List<TeacherProfile> teacherProfileList = new ArrayList<>(List.of(teacherProfile1, teacherProfile2));

        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(new HashSet<>());

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher1), any()))
            .thenReturn(teacherProfile1);

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher2), any()))
            .thenReturn(teacherProfile2);

        when(teacherRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(teacherList));

        assertEquals(teacherProfileList, teacherService.findAll(1,1).getContent());
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

        Teacher teacher1 = teacherList.get(0);
        Teacher teacher2 = teacherList.get(1);

        TeacherProfile teacherProfile1 = new TeacherProfile(
            teacher1.getUserId().toString(),
            teacher1.getFirstName(),
            teacher1.getLastName(),
            teacher1.getEmail(),
            new HashSet<>()
        );
        TeacherProfile teacherProfile2 = new TeacherProfile(
            teacher2.getUserId().toString(),
            teacher2.getFirstName(),
            teacher2.getLastName(),
            teacher2.getEmail(),
            new HashSet<>()
        );

        List<TeacherProfile> teacherProfileList = new ArrayList<>(List.of(teacherProfile1, teacherProfile2));

        when(teacherRepository.findByCourses_CourseName(any(String.class)))
            .thenReturn(teacherList);

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher1), any()))
            .thenReturn(teacherProfile1);

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher2), any()))
            .thenReturn(teacherProfile2);

        when(teacherRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(teacherList));

        assertEquals(teacherProfileList, teacherService.findTeachersByCourse("cn"));
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
        when(teacherRepository.save(any(Teacher.class)))
            .thenReturn(teacher);

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
        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));
        when(courseRepository.findUsersCourses(any(UUID.class)))
            .thenReturn(new HashSet<>(List.of(course)));

        TeacherProfile expectedTeacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new HashSet<>(List.of(new CourseInfo(course.getCourseId().toString() ,course.getCourseName(), course.getCourseDescription())))
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

        when(teacherRepository.save(any(Teacher.class)))
                .thenReturn(new Teacher());

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
        verify(teacherRepository).save(any(Teacher.class));
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

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(passwordValidator)
            .validatePasswordChangeRequest(any(PasswordChangeRequest.class));

        when(teacherRepository.save(any(Teacher.class)))
            .thenReturn(teacher);

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
            "em",
            "pppp",
            "pass",
            "nPass",
            "npass"
        );

        teacherService.changePassword(passwordChangeRequest);

        verify(teacherRepository).findByEmail(any(String.class));
        verify(passwordValidator).validatePasswordChangeRequest(passwordChangeRequest);
        verify(teacherRepository).save(any(Teacher.class));
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

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        doNothing()
            .when(teacherRepository)
            .deleteById(any(UUID.class));

        teacherService.deleteTeacher(teacher.getEmail());

        verify(teacherRepository).findByEmail(teacher.getEmail());
        verify(teacherRepository).deleteById(teacher.getUserId());
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

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(new Course(UUID.randomUUID(), "cn", "cd")));

        teacherService.excludeTeacherFromCourse(teacher.getEmail(), "cn");

        verify(teacherRepository).findByEmail(teacher.getEmail());
        verify(teacherRepository).save(any(Teacher.class));
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

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(new Course(UUID.randomUUID(), "cn", "cd")));

        teacherService.addTeacherToCourse(teacher.getEmail(), "cN");

        verify(teacherRepository).findByEmail(teacher.getEmail());
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void findTeacherByEmailShouldReturnTeacherProfile() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new HashSet<>()
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(teacherProfileMapper.mapTeacherEntityToProfile(eq(teacher), any()))
            .thenReturn(teacherProfile);

        assertEquals(teacherProfile, teacherService.findTeacherByEmail("email"));
    }

}