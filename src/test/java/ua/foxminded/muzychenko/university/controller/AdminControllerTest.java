package ua.foxminded.muzychenko.university.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.muzychenko.university.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.university.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.university.service.AdminService;

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

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;
    @MockBean
    private ParamValidator paramValidator;

    @Test
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
}
