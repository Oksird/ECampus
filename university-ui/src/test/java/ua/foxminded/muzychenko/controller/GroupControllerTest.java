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
import ua.foxminded.muzychenko.service.GroupService;

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

@WebMvcTest(GroupController.class)
@ContextConfiguration(classes = UniversityApplication.class)
@WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupService groupService;
    @MockBean
    private ParamValidator paramValidator;

    @Test
    void indexShouldReturnViewOfAllGroupsByPages() throws Exception {
        int page = 1;
        int size = 5;

        GroupInfo groupInfo1 = new GroupInfo(
            UUID.randomUUID().toString(),
            "gn1",
            1
        );
        GroupInfo groupInfo2 = new GroupInfo(
            UUID.randomUUID().toString(),
            "gn2",
            1
        );

        List<GroupInfo> groupInfoList = new ArrayList<>(List.of(groupInfo1, groupInfo2));

        Page<GroupInfo> groupInfoPage = new PageImpl<>(groupInfoList);

        when(paramValidator.getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));

        when(groupService.findAll(page, size))
            .thenReturn(groupInfoPage);

        mockMvc.perform(get("/groups/"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("groups", groupInfoList))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", groupInfoPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", groupInfoPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("group/groups"));
    }

    @Test
    void indexShouldReturnViewWithGroupPagesFoundByKeyword() throws Exception {
        int page = 1;
        int size = 5;
        String keyword = "AA";

        GroupInfo groupInfo1 = new GroupInfo(
            UUID.randomUUID().toString(),
            "gn1",
            1
        );
        GroupInfo groupInfo2 = new GroupInfo(
            UUID.randomUUID().toString(),
            "gn2",
            1
        );

        List<GroupInfo> groupInfoList = new ArrayList<>(List.of(groupInfo1, groupInfo2));

        Page<GroupInfo> groupInfoPage = new PageImpl<>(groupInfoList);

        when(paramValidator.getValidatedPageRequest(anyString(), anyString()))
            .thenReturn(Map.of("page", page, "size", size));

        when(groupService.findGroupsPagesByNamePart(keyword, page, size))
            .thenReturn(groupInfoPage);

        mockMvc.perform(get("/groups/?keyword=AA"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("keyword", keyword))
            .andExpect(model().attribute("groups", groupInfoList))
            .andExpect(model().attribute("currentPage", page))
            .andExpect(model().attribute("totalItems", groupInfoPage.getTotalElements()))
            .andExpect(model().attribute("totalPages", groupInfoPage.getTotalPages()))
            .andExpect(model().attribute("pageSize", String.valueOf(size)))
            .andExpect(view().name("group/groups"));
    }
}
