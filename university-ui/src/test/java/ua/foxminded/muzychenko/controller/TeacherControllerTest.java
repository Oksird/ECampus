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
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.service.TeacherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TeacherController.class)
@ContextConfiguration(classes = UniversityApplication.class)
@WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private ParamValidator paramValidator;

    @Test
    void indexShouldReturnAllAdminsByPages() throws Exception {
        int page = 1;
        int size = 5;

        CourseInfo courseInfo = new CourseInfo(
            "id",
            "cn",
            "cd"
        );

        TeacherProfile teacherProfile1 = new TeacherProfile(
            "id1",
            "fN1",
            "lN1",
            "em1",
            new HashSet<>(Collections.singleton(courseInfo))
        );
        TeacherProfile teacherProfile2 = new TeacherProfile(
            "id2",
            "fN2",
            "lN2",
            "em2",
            new HashSet<>(Collections.singleton(courseInfo))
        );

        List<TeacherProfile> teacherProfiles = new ArrayList<>(List.of(teacherProfile1, teacherProfile2));

        Page<TeacherProfile> teacherPage = new PageImpl<>(teacherProfiles);

        when(paramValidator
            .getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));
        when(teacherService
            .findAll(page, size))
            .thenReturn(teacherPage);

        mockMvc.perform(get("/teachers/")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("teachers", teacherProfiles))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", teacherPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", teacherPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("teacher/teachers"));
    }
}
