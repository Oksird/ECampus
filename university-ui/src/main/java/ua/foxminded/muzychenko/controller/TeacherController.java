package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.service.TeacherService;

import java.util.Map;

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
}
