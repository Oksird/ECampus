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
import ua.foxminded.muzychenko.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CourseController.class)
@ContextConfiguration(classes = UniversityApplication.class)
@WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourseService courseService;
    @MockBean
    private ParamValidator paramValidator;

    @Test
    void indexShouldReturnViewOfAllCoursesByPages() throws Exception {
        int page = 1;
        int size = 5;

        CourseInfo courseInfo1 = new CourseInfo(
            UUID.randomUUID().toString(),
            "cn1",
            "cd1"
        );
        CourseInfo courseInfo2 = new CourseInfo(
            UUID.randomUUID().toString(),
            "cn2",
            "cd2"
        );

        List<CourseInfo> courseInfoList = new ArrayList<>(List.of(courseInfo1, courseInfo2));

        Page<CourseInfo> courseInfoPage = new PageImpl<>(courseInfoList);

        when(paramValidator.getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));

        when(courseService.findAll(page, size))
            .thenReturn(courseInfoPage);

        mockMvc.perform(get("/courses/"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("courses", courseInfoList))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", courseInfoPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", courseInfoPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("course/courses"));
    }

    @Test
    void indexShouldReturnViewWithPagesFoundByKeyword() throws Exception {
        int page = 1;
        int size = 5;
        String keyword = "Course";

        CourseInfo courseInfo1 = new CourseInfo(
            UUID.randomUUID().toString(),
            "cn1",
            "cd1"
        );
        CourseInfo courseInfo2 = new CourseInfo(
            UUID.randomUUID().toString(),
            "cn2",
            "cd2"
        );

        List<CourseInfo> courseInfoList = new ArrayList<>(List.of(courseInfo1, courseInfo2));

        Page<CourseInfo> courseInfoPage = new PageImpl<>(courseInfoList);

        when(paramValidator.getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));

        when(courseService.findCoursesPagesByNamePart(keyword, page, size))
            .thenReturn(courseInfoPage);

        mockMvc.perform(get("/courses/?keyword=Course"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("courses", courseInfoList))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", courseInfoPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", courseInfoPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("course/courses"));
    }
}
