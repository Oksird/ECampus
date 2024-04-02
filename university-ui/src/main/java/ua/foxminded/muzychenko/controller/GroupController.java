package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.service.GroupService;
import ua.foxminded.muzychenko.service.StudentService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final ParamValidator paramValidator;
    private final StudentService studentService;

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

    @GetMapping("/{groupId}")
    public String getProfileById(@PathVariable("groupId") UUID id, Model model) {
        GroupInfo groupInfo = groupService.findGroupById(id);
        List<StudentProfile> students = studentService.findStudentsByGroup(groupInfo.getGroupName());

        model.addAttribute("group", groupInfo);
        model.addAttribute("students", students);

        return "group/profile";
    }

    @GetMapping("/group{groupName}")
    public String getProfileByName(@PathVariable("groupName") String groupName, Model model) {
        GroupInfo groupInfo = groupService.findGroupByName(groupName);
        List<StudentProfile> students = studentService.findStudentsByGroup(groupInfo.getGroupName());
        List<CourseInfo> courses = groupService.findGroupCourses(groupName);

        model.addAttribute("group", groupInfo);
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);

        return "group/profile";
    }

    @GetMapping("/new")
    public String createGroup(Model model) {
        GroupInfo groupInfo = new GroupInfo();

        model.addAttribute("groupInfo", groupInfo);

        return "group/create_group";
    }

    @PostMapping("/")
    public String createGroup(@ModelAttribute GroupInfo groupInfo) {
        groupService.createGroup(groupInfo);
        return "redirect:/groups/new";
    }

    @GetMapping("/get-courses-for-group")
    @ResponseBody
    public List<String> getCourseNamesForGroup(@RequestParam String groupName) {
        GroupInfo groupInfo = groupService.findGroupByName(groupName);
        return groupInfo.getCourses().stream().map(CourseInfo::getCourseName).toList();
    }
}
