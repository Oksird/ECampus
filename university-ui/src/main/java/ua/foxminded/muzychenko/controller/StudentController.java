package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.service.StudentService;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<StudentProfile> studentPage = studentService.findAll(pageCount, pageSize);

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", studentPage.getNumber() + 1);
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "student/students";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        StudentProfile studentProfile = studentService.findStudentByEmail(userDetails.getUsername());

        model.addAttribute("profile", studentProfile);

        return "student/profile";
    }

    @GetMapping("/{userId}")
    public String getProfileById(@PathVariable("userId") UUID id, Model model) {
        StudentProfile studentProfile = studentService.findStudentById(id);

        model.addAttribute("profile", studentProfile);

        return "student/profile";
    }

    @GetMapping("/my-courses")
    public String getMyCourses(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        StudentProfile studentProfile = studentService.findStudentByEmail(userDetails.getUsername());

        model.addAttribute("courses", studentProfile.getCoursesInfo());

        return "course/user-courses";
    }

    @GetMapping("/{userId}/courses")
    public String getCoursesByStudentId(@PathVariable("userId") UUID id, Model model) {
        StudentProfile studentProfile = studentService.findStudentById(id);

        model.addAttribute("courses", studentProfile.getCoursesInfo());

        return "course/user-courses";
    }

    @GetMapping("/my-group")
    public String getMyGroup(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        StudentProfile studentProfile = studentService.findStudentByEmail(userDetails.getUsername());

        model.addAttribute("group", studentProfile.getGroupInfo());

        return "group/profile";
    }

    @GetMapping("/{userId}/group")
    public String getGroupByStudentId(@PathVariable("userId") UUID id, Model model) {
        StudentProfile studentProfile = studentService.findStudentById(id);

        model.addAttribute("group", studentProfile.getGroupInfo());

        return "group/profile";
    }
}
