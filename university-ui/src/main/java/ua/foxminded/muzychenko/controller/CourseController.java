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
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.service.CourseService;
import ua.foxminded.muzychenko.service.GroupService;
import ua.foxminded.muzychenko.service.TeacherService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ParamValidator paramValidator;
    private final GroupService groupService;
    private final TeacherService teacherService;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String keyword,
                        @RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<CourseInfo> coursesPage;

        if (keyword == null) {
            coursesPage = courseService.findAll(pageCount, pageSize);
        } else {
            coursesPage = courseService.findCoursesPagesByNamePart(keyword, pageCount, pageSize);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("courses", coursesPage.getContent());
        model.addAttribute("currentPage", coursesPage.getNumber() + 1);
        model.addAttribute("totalItems", coursesPage.getTotalElements());
        model.addAttribute("totalPages", coursesPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "course/courses";
    }

    @GetMapping("/{courseId}")
    public String getProfileById(@PathVariable("courseId") UUID id, Model model) {
        CourseInfo courseInfo = courseService.findCourseById(id);

        return getCourseProfileView(model, courseInfo);
    }

    @GetMapping("/course{courseName}")
    public String getProfileByName(@PathVariable("courseName") String courseName, Model model) {
        CourseInfo courseInfo = courseService.findCourseByName(courseName);

        return getCourseProfileView(model, courseInfo);
    }

    @GetMapping("/new")
    public String createCourse(Model model) {
        List<TeacherProfile> teacherProfiles = teacherService.findAll();

        model.addAttribute("teachers", teacherProfiles);
        model.addAttribute("courseInfo", new CourseInfo());

        return "course/create_course";
    }

    @PostMapping("/")
    private String createCourse(@ModelAttribute CourseInfo courseInfo) {
        UUID teacherId = UUID.fromString(courseInfo.getTeacherProfile().getUserId());

        TeacherProfile teacherProfile = teacherService.findTeacherById(teacherId);

        courseInfo.setTeacherProfile(teacherProfile);

        courseService.createCourse(courseInfo, teacherProfile.getEmail());
        return "redirect:/courses/new";
    }

    private String getCourseProfileView(Model model, CourseInfo courseInfo) {
        TeacherProfile teacher = courseInfo.getTeacherProfile();
        List<GroupInfo> groups = groupService.findGroupsByCourse(courseInfo);

        model.addAttribute("course", courseInfo);
        model.addAttribute("teacher", teacher);
        model.addAttribute("groups", groups);

        return "course/profile";
    }
}
