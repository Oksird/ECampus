package ua.foxminded.muzychenko.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.muzychenko.UniversityApplication;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.dto.profile.StaffProfile;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.service.AdminService;
import ua.foxminded.muzychenko.service.CourseService;
import ua.foxminded.muzychenko.service.GroupService;
import ua.foxminded.muzychenko.service.LessonService;
import ua.foxminded.muzychenko.service.PendingUserService;
import ua.foxminded.muzychenko.service.StaffService;
import ua.foxminded.muzychenko.service.StudentService;
import ua.foxminded.muzychenko.service.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
@ContextConfiguration(classes = UniversityApplication.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;
    @MockBean
    private ParamValidator paramValidator;
    @MockBean
    private PendingUserService pendingUserService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private StaffService staffService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private LessonService lessonService;


    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"ADMIN"})
    void indexShouldReturnAllAdminsByPages() throws Exception {
        int page = 1;
        int size = 5;

        AdminProfile adminProfile1 = new AdminProfile(
            UUID.randomUUID().toString(),
            "fn1",
            "ln1",
            "em1"
            );
        AdminProfile adminProfile2 = new AdminProfile(
            UUID.randomUUID().toString(),
            "fn2",
            "ln2",
            "em2"
        );

        List<AdminProfile> adminProfiles = new ArrayList<>(List.of(adminProfile1, adminProfile2));

        Page<AdminProfile> adminPage = new PageImpl<>(adminProfiles);

        when(paramValidator
            .getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));
        when(adminService
            .findAll(page, size))
            .thenReturn(adminPage);

        mockMvc.perform(get("/admins/")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("admins", adminProfiles))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", adminPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", adminPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("admin/admins"));
    }

    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"ADMIN"})
    void changeUserRoleShouldChangeUsersRoleToAndRedirectToStudentAdminPanel() throws Exception {
        UUID userId = UUID.randomUUID();
        String role = "Student";

        PendingUserProfile profile = new PendingUserProfile(
            userId.toString(),
            "fn",
            "ln",
            "em"
        );

        when(pendingUserService.findById(userId)).thenReturn(profile);

        doNothing()
            .when(studentService)
            .createStudentFromPendingUser(profile);

        mockMvc.perform(post("/admins/change-user-role/{userId}", userId)
                .param("role", role)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admins/cpanel"));

        verify(studentService).createStudentFromPendingUser(profile);
    }

    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"ADMIN"})
    void changeUserRoleShouldChangeUsersRoleToTeacherAndRedirectToAdminPanel() throws Exception {
        UUID userId = UUID.randomUUID();
        String role = "Teacher";

        PendingUserProfile profile = new PendingUserProfile(
            userId.toString(),
            "fn",
            "ln",
            "em"
        );

        when(pendingUserService.findById(userId)).thenReturn(profile);

        doNothing()
            .when(teacherService)
            .createTeacherFromPendingUser(profile);

        mockMvc.perform(post("/admins/change-user-role/{userId}", userId)
                .param("role", role)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admins/cpanel"));

        verify(teacherService).createTeacherFromPendingUser(profile);
    }

    @Test
    @WithMockUser(username = "admin@adm.com", roles = {"ADMIN"})
    void changeUserRoleShouldChangeUsersRoleToStaffAndRedirectToAdminPanel() throws Exception {
        UUID userId = UUID.randomUUID();
        String role = "Staff";

        PendingUserProfile profile = new PendingUserProfile(
            userId.toString(),
            "fn",
            "ln",
            "em"
        );

        when(pendingUserService.findById(userId)).thenReturn(profile);

        doNothing()
            .when(staffService)
            .createStaffFromPendingUser(profile);

        mockMvc.perform(post("/admins/change-user-role/{userId}", userId)
                .param("role", role)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admins/cpanel"));

        verify(staffService).createStaffFromPendingUser(profile);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void controlPanelShouldReturnAdminsControlPanel() throws Exception {

        int pageCount = 1;
        int pageSize = 5;

        Page<PendingUserProfile> pendingUserProfiles = new PageImpl<>(new ArrayList<>());
        Page<StudentProfile> studentProfiles = new PageImpl<>(new ArrayList<>());
        Page<TeacherProfile> teacherProfiles = new PageImpl<>(new ArrayList<>());
        Page<StaffProfile> staffProfiles = new PageImpl<>(new ArrayList<>());
        Page<GroupInfo> groupInfos = new PageImpl<>(new ArrayList<>());
        Page<CourseInfo> courseInfos = new PageImpl<>(new ArrayList<>());
        Page<LessonInfo> lessonInfos = new PageImpl<>(new ArrayList<>());

        when(pendingUserService.findAll(pageCount, pageSize)).thenReturn(pendingUserProfiles);
        when(studentService.findAll(pageCount, pageSize)).thenReturn(studentProfiles);
        when(teacherService.findAll(pageCount, pageSize)).thenReturn(teacherProfiles);
        when(staffService.findAll(pageCount, pageSize)).thenReturn(staffProfiles);
        when(groupService.findAll(pageCount, pageSize)).thenReturn(groupInfos);
        when(courseService.findAll(pageCount, pageSize)).thenReturn(courseInfos);
        when(lessonService.findAll(pageCount, pageSize)).thenReturn(lessonInfos);

        mockMvc.perform(get("/admins/cpanel"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/cpanel"))
            .andExpect(model().attributeExists("pendingUsers", "students", "teachers", "staffProfiles", "groups", "courses", "lessons", "totalItems"))
            .andExpect(model().attribute("pendingUsers", pendingUserProfiles.getContent()))
            .andExpect(model().attribute("students", studentProfiles.getContent()))
            .andExpect(model().attribute("teachers", teacherProfiles.getContent()))
            .andExpect(model().attribute("staffProfiles", staffProfiles.getContent()))
            .andExpect(model().attribute("groups", groupInfos.getContent()))
            .andExpect(model().attribute("courses", courseInfos.getContent()))
            .andExpect(model().attribute("lessons", lessonInfos.getContent()))
            .andExpect(model().attribute("totalItems", pageSize));
    }
}
