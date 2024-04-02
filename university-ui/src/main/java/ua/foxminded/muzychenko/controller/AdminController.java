package ua.foxminded.muzychenko.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.*;
import ua.foxminded.muzychenko.service.*;

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
    private final ScheduleService scheduleService;

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

        model.addAttribute("pendingUsers", pendingUserProfiles.getContent());
        model.addAttribute("students", studentProfiles.getContent());
        model.addAttribute("teachers", teacherProfiles.getContent());
        model.addAttribute("staffProfiles", staffProfiles.getContent());
        model.addAttribute("groups", groupInfos.getContent());
        model.addAttribute("courses", courseInfos.getContent());
        model.addAttribute("totalItems", pageSize);

        return "admin/cpanel";
    }

    @PostMapping("/change-user-role/{userId}")
    public String changeUserRole(@RequestParam("role") String role,
                                 @PathVariable("userId") String userId, HttpServletRequest request) {

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

        String currentURI = request.getRequestURI();

        return "redirect:" + currentURI;
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        AdminProfile adminProfile = adminService.findAdminByEmail(userDetails.getUsername());

        model.addAttribute("profile", adminProfile);

        return "admin/profile";
    }

    @GetMapping("/{userId}")
    public String getProfileById(@PathVariable("userId") UUID id, Model model) {
        AdminProfile profile = adminService.findAdminById(id);

        model.addAttribute("profile", profile);

        return "admin/profile";
    }
}
