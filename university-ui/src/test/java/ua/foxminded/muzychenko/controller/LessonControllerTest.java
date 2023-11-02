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
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.service.LessonService;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(LessonController.class)
@ContextConfiguration(classes = UniversityApplication.class)
@WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private ParamValidator paramValidator;

    @Test
    void indexShouldReturnAllLessonsByPages() throws Exception {
        int page = 1;
        int size = 5;

        CourseInfo courseInfo = new CourseInfo(
            "id",
            "cn",
            "cd"
            );

        TeacherProfile teacherProfile = new TeacherProfile(
            "id",
            "name",
            "lName",
            "email",
            new HashSet<>(Collections.singleton(courseInfo))
        );

        LessonInfo lessonInfo1 = new LessonInfo(
            UUID.randomUUID().toString(),
            "cn",
            teacherProfile,
            "gn",
            Date.valueOf("2022-09-01"),
            Time.valueOf("08:30:00"),
            Time.valueOf("09:30:00")
        );

        LessonInfo lessonInfo2 = new LessonInfo(
            UUID.randomUUID().toString(),
            "cn2",
            teacherProfile,
            "gn2",
            Date.valueOf("2022-09-02"),
            Time.valueOf("09:30:00"),
            Time.valueOf("10:30:00")
        );

        List<LessonInfo> lessonInfos = new ArrayList<>(List.of(lessonInfo1, lessonInfo2));

        Page<LessonInfo> lessonPage = new PageImpl<>(lessonInfos);

        when(paramValidator
            .getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));
        when(lessonService
            .findAll(page, size))
            .thenReturn(lessonPage);

        mockMvc.perform(get("/lessons/")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("lessons", lessonInfos))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", lessonPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", lessonPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("lesson/lessons"));
    }
}
