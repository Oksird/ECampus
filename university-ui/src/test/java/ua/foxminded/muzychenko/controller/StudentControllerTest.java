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
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StudentController.class)
@ContextConfiguration(classes = UniversityApplication.class)
@WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @MockBean
    private ParamValidator paramValidator;

    @Test
    void indexShouldReturnAllAdminsByPages() throws Exception {
        int page = 1;
        int size = 5;

        List<StudentProfile> studentProfiles = getStudentProfiles();

        Page<StudentProfile> studentPage = new PageImpl<>(studentProfiles);

        when(paramValidator
            .getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));
        when(studentService
            .findAll(page, size))
            .thenReturn(studentPage);

        mockMvc.perform(get("/students/")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("students", studentProfiles))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", studentPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", studentPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("student/students"));
    }

    private static List<StudentProfile> getStudentProfiles() {
        GroupInfo groupInfo = new GroupInfo(
            "id",
            "gn",
            1,
            null
        );

        StudentProfile studentProfile1 = new StudentProfile(
            "id1",
            "fN1",
            "lN1",
            "em1",
            groupInfo,
            "38066",
            "addr"
        );

        StudentProfile studentProfile2 = new StudentProfile(
            "id2",
            "fN2",
            "lN2",
            "em2",
            groupInfo,
            "38066",
            "addr"
        );

        return new ArrayList<>(List.of(studentProfile1, studentProfile2));
    }
}
