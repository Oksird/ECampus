package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.service.StudentService;

import java.util.Map;

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
}
