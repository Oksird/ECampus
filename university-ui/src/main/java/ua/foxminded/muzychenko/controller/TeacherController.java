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
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.service.TeacherService;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<TeacherProfile> teacherPage = teacherService.findAll(pageCount, pageSize);

        model.addAttribute("teachers", teacherPage.getContent());
        model.addAttribute("currentPage", teacherPage.getNumber() + 1);
        model.addAttribute("totalItems", teacherPage.getTotalElements());
        model.addAttribute("totalPages", teacherPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "teacher/teachers";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        TeacherProfile teacherProfile = teacherService.findTeacherByEmail(userDetails.getUsername());

        model.addAttribute("profile", teacherProfile);

        return "teacher/profile";
    }

    @GetMapping("/{userId}")
    public String getProfileById(@PathVariable("userId") UUID id, Model model) {
        TeacherProfile teacherProfile = teacherService.findTeacherById(id);

        model.addAttribute("profile", teacherProfile);

        return "teacher/profile";
    }

    @GetMapping("/my-courses")
    public String getMyCourses(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        TeacherProfile teacherProfile = teacherService.findTeacherByEmail(userDetails.getUsername());

        model.addAttribute("courses", teacherService.getTeacherCourses(teacherProfile.getEmail()));

        return "course/user-courses";
    }

    @GetMapping("/{userId}/courses")
    public String getCoursesByTeacherId(@PathVariable("userId") UUID id, Model model) {
        TeacherProfile teacherProfile = teacherService.findTeacherById(id);

        model.addAttribute("courses", teacherService.getTeacherCourses(teacherProfile.getEmail()));
        //TODO: change teacher profile - add course info
        return "course/user-courses";
    }
}
