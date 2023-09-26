package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.service.CourseService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ParamValidator paramValidator;

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
}
