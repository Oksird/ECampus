package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;
    private final PendingUserService pendingUserService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StaffService staffService;
    private final ParamValidator paramValidator;
    private final GroupService groupService;
    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<AdminProfile> adminPage = adminService.findAll(pageCount, pageSize);

        model.addAttribute("admins", adminPage.getContent());
        model.addAttribute("currentPage", adminPage.getNumber() + 1);
        model.addAttribute("totalItems", adminPage.getTotalElements());
        model.addAttribute("totalPages", adminPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/admins";
    }

    @GetMapping("/cpanel")
    String controlPanel(Model model) {
        int pageCount = 1;
        int pageSize = 5;

        Page<PendingUserProfile> pendingUserProfiles = pendingUserService.findAll(pageCount, pageSize);
        Page<StudentProfile> studentProfiles = studentService.findAll(pageCount, pageSize);
        Page<TeacherProfile> teacherProfiles = teacherService.findAll(pageCount, pageSize);
        Page<StaffProfile> staffProfiles = staffService.findAll(pageCount, pageSize);
        Page<GroupInfo> groupInfos = groupService.findAll(pageCount, pageSize);
        Page<CourseInfo> courseInfos = courseService.findAll(pageCount, pageSize);
        Page<LessonInfo> lessonInfos = lessonService.findAll(pageCount, pageSize);

        model.addAttribute("pendingUsers", pendingUserProfiles.getContent());
        model.addAttribute("students", studentProfiles.getContent());
        model.addAttribute("teachers", teacherProfiles.getContent());
        model.addAttribute("staffProfiles", staffProfiles.getContent());
        model.addAttribute("groups", groupInfos.getContent());
        model.addAttribute("courses", courseInfos.getContent());
        model.addAttribute("lessons", lessonInfos.getContent());
        model.addAttribute("totalItems", pageSize);

        return "admin/cpanel";
    }

    @PostMapping("/change-user-role/{userId}")
    public String changeUserRole(@RequestParam("role") String role, @PathVariable("userId") String userId) {

        PendingUserProfile profile = pendingUserService.findById(UUID.fromString(userId));

        switch (role) {
            case "Student":
                studentService.createStudentFromPendingUser(profile);
                break;
            case "Teacher":
                teacherService.createTeacherFromPendingUser(profile);
                break;
            case "Staff":
                staffService.createStaffFromPendingUser(profile);
                break;
            default:
                break;
        }

        return "redirect:/admins/cpanel";
    }
}
