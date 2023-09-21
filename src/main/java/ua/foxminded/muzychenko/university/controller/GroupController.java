package ua.foxminded.muzychenko.university.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.university.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.service.GroupService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String keyword,
                        @RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<GroupInfo> groupsPage;

        if (keyword == null) {
            groupsPage = groupService.findAll(pageCount, pageSize);
        } else {
            groupsPage = groupService.findGroupsPagesByNamePart(keyword, pageCount, pageSize);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("groups", groupsPage.getContent());
        model.addAttribute("currentPage", groupsPage.getNumber() + 1);
        model.addAttribute("totalItems", groupsPage.getTotalElements());
        model.addAttribute("totalPages", groupsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "group/groups";
    }
}
