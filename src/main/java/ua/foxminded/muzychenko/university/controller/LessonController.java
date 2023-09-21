package ua.foxminded.muzychenko.university.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.university.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.university.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.university.service.LessonService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1")  String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<LessonInfo> lessonPage = lessonService.findAll(pageCount, pageSize);

        model.addAttribute("lessons", lessonPage.getContent());
        model.addAttribute("currentPage", lessonPage.getNumber() + 1);
        model.addAttribute("totalItems", lessonPage.getTotalElements());
        model.addAttribute("totalPages", lessonPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "lesson/lessons";
    }
}
